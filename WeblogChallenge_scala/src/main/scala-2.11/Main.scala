
import org.apache.spark.{SparkContext, SparkConf}
import javax.xml.bind.DatatypeConverter
import scala.collection.mutable._
import java.util.Date
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

object Main {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Weblog Challenge").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    val rdd = sc.textFile("data/2015_07_22_mktplace_shop_web_log_sample.log.gz").map(line => line.split(" "))
    val rdd_parsed = rdd.map(x => (x(2).split(":")(0), (DatatypeConverter.parseDateTime(x(0)).getTime, x(12))))

    val initialList = ListBuffer.empty[(Date, String)]
    val addToList = (s: ListBuffer[(Date, String)], v: (Date, String)) => s += v
    val mergePartitionLists = (p1: ListBuffer[(Date, String)], p2: ListBuffer[(Date, String)]) => p1 ++= p2
    val grouped = rdd_parsed.aggregateByKey(initialList)(addToList, mergePartitionLists)
    val grouped_sorted = grouped.map(x => (x._1, x._2.toList.sortBy(_._1)))

    val sessionized = grouped_sorted.mapValues(x => getSessionLengthAndUrls(x))
      .sortBy(_._2.size, false)


    val sessionized_total = sessionized.mapValues(x => calculateSumSessionLenAndUrl(x))
    sessionized_total.cache()
    sessionized_total.mapValues(x => (x._1/x._3, x._2/x._3)).sortBy(_._2._1,false).take(10).foreach(println)

    val (totalSessLen, totalUrls, totalSess) = sessionized_total.map(_._2).reduce((v1,v2) => (v1._1+v2._1, v1._2+v2._2, v1._3+v2._3))
    println(totalSessLen/totalSess)
    println(totalUrls/totalSess)
  }

  def timeExpired(timeWindow: TimeWindow, duration: Duration = Duration(15, TimeUnit.MINUTES)): Boolean = Math.abs(timeWindow.endTime.getTime - timeWindow.startTime.getTime) > duration.toMillis


  def sessionize(timestamps: List[(Date, String)], // a sorted List of (Date,String) tuples
                 prevTime: Date, // timestamp previously added to the accumulator
                 sessions: List[List[(Date, String)]]): List[List[(Date, String)]] = {
    timestamps match {
      // input is empty, return the accumulator
      case Nil => sessions
      // input has a head and tail, head is close to previous timestamp
      // start again with new input (current tail), new previous (current head),
      // and the current head inserted into accumulator
      case h :: t if !timeExpired(TimeWindow(prevTime, h._1)) => sessionize(t, h._1, (h :: sessions.head) :: sessions.tail)

      // input has a head and tail, head is not close to previous timestamp
      // start again with new input (current tail), new previous (current head),
      // and the current head is the start of a new sub-list in the accumulator
      case h :: t => sessionize(t, h._1, List(h) :: sessions)
    }
  }

  def getSessionLengthAndUrls(timestamps: List[(Date, String)]): List[(Float,Int)] = {
    sessionize(timestamps, timestamps.head._1, List(List.empty[(Date, String)])).map {
      case timestampList =>
        val len =
          if (timestampList.size == 1)
           0
        else
          Math.abs(timestampList.head._1.getTime - timestampList.last._1.getTime).toFloat / 60000

        (len, timestampList.map(_._2).distinct.size)
    }
  }

  def calculateSumSessionLenAndUrl(sessions: List[(Float,Int)]): (Double,Double,Int) = {
    val sum = sessions.foldLeft((0.0, 0.0)) { case ((accA, accB), (a, b)) => (accA + a, accB + b) }
    (sum._1,sum._2,sessions.size)
  }
}
