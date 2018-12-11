
## Processing & Analytical goals:

1. Sessionize the web log by IP. Sessionize = aggregrate all page hits by visitor/IP during a session.

Example: User with IP '220.226.206.7' has 13 sessions in total, with the longest session being 34 minutes.

('220.226.206.7', <br />
  [3.0329445333333336,<br />
   3.0448327333333336,<br />
   4.056144683333334,<br />
   4.024646466666667,<br />
   34.0800585,<br />
   4.8486105833333335,<br />
   4.8596510833333335,<br />
   4.173275283333333,<br />
   13.028803033333332,<br />
   4.2956905,<br />
   25.08101915,<br />
   4.992088083333333,<br />
   3.7483676833333335])<br />

2. Determine the average session time

The average length of all sessions is 1.678791824697907 minutes.

3. Determine unique URL visits per session. To clarify, count a hit to a unique URL only once per session.

Example: user with IP '220.226.206.7' has 13 sessions in total, where the largest number of URL among all sessions is 105.
('220.226.206.7',
  [7, 5, 25, 13, 42, 15, 61, 105, 50, 92, 284, 30, 163])

The average number of unique URL of all sessions is 8.314305563074036.

4. Find the most engaged users, ie the IPs with the longest session times

('103.29.159.138', 34.42355186666667),<br />
 ('125.16.218.194', 34.407923399999994),<br />
 ('14.99.226.79', 34.381827933333334),<br />
 ('122.169.141.4', 34.3359822),<br />
 ('14.139.220.98', 34.305317716666664), <br />
 ('117.205.158.11', 34.287053066666665),<br />
 ('111.93.89.14', 34.249600699999995),<br />
 ('182.71.63.42', 34.17429933333334),<br />
 ('223.176.3.130', 34.128679416666664),<br />
 ('183.82.103.131', 34.045396966666665)<br />

The 10 most engaged users ranked by average session length. 



## Additional questions for Machine Learning Engineer (MLE) candidates:
1. Predict the expected load (requests/second) in the next minute

![Alt text](1.png?raw=true "Number of requests per minute")

We first count the number of requests in each minute, if there are no requests in some minutes, we set the count to be zero then created the above graph. It can be seen from the graph that the number of requests between 6:00 and 7:00, 12:00 and 13:00 is much higher than the rest time period. <br />

To predict the expected load in the next minute y(t+1), we first think about using the ARIMA model. From the first graph it is obivous this time series is not stationary, we applied square transformation to each y(t) then calculate y(t)-y(t-1) to have a new time series, 

![Alt text](3.png?raw=true "y(t)-y(t-1)")

unfortunately the new time series is still not stationary, meaning we cannot use ARIMA model.

![Alt text](2.png?raw=true "y(t) vs y(t+1)")

When we plot y(t) vs y(t+1), we observe that there is a strong linear relationship between y(t) and y(t+1), to simplify our model, we just use y(t) as the predictor of y(t+1). Note that y(t) is the number of requests in 1 minute, we divided it by 60 to have the average number of requests per second. The following shows part of predicted load per second in each minute, starting from 22:41:00.

2015-07-21 22:41:00    113.116667 <br />
2015-07-21 22:42:00     93.100000 <br />
2015-07-21 22:43:00     78.900000 <br />
2015-07-21 22:44:00     78.000000 <br />
2015-07-21 22:45:00      5.383333 <br />
2015-07-21 22:46:00      0.000000 <br />
2015-07-21 22:47:00      0.000000 <br />
2015-07-21 22:48:00      0.000000 <br />
2015-07-21 22:49:00      0.000000 <br />
2015-07-21 22:50:00      0.000000 <br />
2015-07-21 22:51:00      0.000000 <br />
2015-07-21 22:52:00      0.000000 <br />

2. Predict the session length for a given IP

We use the average value of session length of any IP as the predicted session length of that IP, the following shows part of the prediction results:

('103.29.159.138', 34.42355186666667),<br />
 ('125.16.218.194', 34.407923399999994),<br />
 ('14.99.226.79', 34.381827933333334),<br />
 ('122.169.141.4', 34.3359822),<br />
 ('14.139.220.98', 34.305317716666664), <br />
 ('117.205.158.11', 34.287053066666665),<br />

3. Predict the number of unique URL visits by a given IP

We use the average value of unique URL visits of any IP as the predicted unique URL visits of that IP, the following shows part of the prediction results:

('52.74.219.71', 3211.9), <br />
 ('119.81.61.166', 2954.1), <br />
 ('106.51.132.54', 2609.0), <br />
 ('106.186.23.95', 1375.8), <br />
 ('54.169.0.163', 859.3333333333334), <br />
 ('54.169.64.74', 825.0), <br />
 ('54.169.20.106', 779.2857142857143), <br />
 ('54.169.79.96', 747.3333333333334), <br />
 ('54.169.164.205', 735.5714285714286), <br />
 ('54.251.128.29', 702.5) <br />