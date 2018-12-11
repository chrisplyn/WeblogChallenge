
## Processing & Analytical goals:

1. Sessionize the web log by IP. Sessionize = aggregrate all page hits by visitor/IP during a session.
Example: User with IP '220.226.206.7' has 13 sessions in total, with the longest session being 34 minutes.
('220.226.206.7',
  [3.0329445333333336,
   3.0448327333333336,
   4.056144683333334,
   4.024646466666667,
   34.0800585,
   4.8486105833333335,
   4.8596510833333335,
   4.173275283333333,
   13.028803033333332,
   4.2956905,
   25.08101915,
   4.992088083333333,
   3.7483676833333335])

2. Determine the average session time
The average length of all sessions is 1.678791824697907 minutes.

3. Determine unique URL visits per session. To clarify, count a hit to a unique URL only once per session.
Example: user with IP '220.226.206.7' has 13 sessions in total, where the largest number of URL among all sessions is 105.
('220.226.206.7',
  [7, 5, 25, 13, 42, 15, 61, 105, 50, 92, 284, 30, 163])

The average number of unique URL of all sessions is 8.314305563074036.

4. Find the most engaged users, ie the IPs with the longest session times
('103.29.159.138', 34.42355186666667),
 ('125.16.218.194', 34.407923399999994),
 ('14.99.226.79', 34.381827933333334),
 ('122.169.141.4', 34.3359822),
 ('14.139.220.98', 34.305317716666664),
 ('117.205.158.11', 34.287053066666665),
 ('111.93.89.14', 34.249600699999995),
 ('182.71.63.42', 34.17429933333334),
 ('223.176.3.130', 34.128679416666664),
 ('183.82.103.131', 34.045396966666665)
The 10 most engaged users ranked by average session length. 



## Additional questions for Machine Learning Engineer (MLE) candidates:
1. Predict the expected load (requests/second) in the next minute

2. Predict the session length for a given IP

3. Predict the number of unique URL visits by a given IP

