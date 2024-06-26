# domiot-webservice
This project is a web service that handles all web requests of the domotics eco-system.

# Swagger UI
http://<host>:<port>/api/swagger-ui/index.html#

Requirements sensor values:
As domiot user I want to show a graph of single and multiple sensor values for a specific time range.
This time range can be: Daily, Hourly, Weekly, Monthly, Yearly
For every time range it is possible to zoom in. This will require a new query of data.

Features:
* Serve sensor values from domiot database
* Serve the same as a list of graph point objects

## Graph data requirements
Serving graph data has a few more requirements:
* For combined graph data (data that needs to be shown in one graph, e.g. AC and AP Power) regular identical timestamps with values need to be present. 
The P1 sensorvalue sender has an algorithm that makes sure that every minute a value is sent.
This value on the minute boarder must be present in the graph data set in order for all the combined graphs to be shown.

Determining the number of data points is specified as follows:
* The reference smartphone is 1280 pixels high (landscape modus)
* 3/4 of the width will be used for graph data which is 960 pixels. The other part is considered margins etc.
* Not on every pixel a data point is needed. At max 1 point per 4 pixels, which makes it 240. With this number, lots of data points can be left out. 
* For every data point a representant of all the combined graphs will need to be present. This requires interpolation of data points if not all values are on the same timestamp.
* The interpolated graph data shall be calculated offline and saved in the database. 

### Data example for serving graph data for Actual Consumed and Actual Produced power
When the request has arrived (two sensorIds, a start and end timestamp denoting one day) the data for that time frame is retrieved from the database. It will contain up to 86,400 data points for AC, considering a value each second. AP will have significantly fewer data points while data is only produced at day time with addition of the inserted zero values at minute intervals. 
When requiring only 240 data points, each 6 minute a value is needed for every sensor in the query at exact the same timestamp.
This will be done as follows:
Iterate through all AC values, taking a value in the result set every 6 minutes. For every value, look for the same time stamp in the AP values set. If it is found, add it to the result set. If not, take two values that are before and after the timestamp and interpolate a new value. 

# Features to be expected:
* CRUD actions for sites, rooms, devices, sensors, users, roles and permissions
* Serving domiot-rest-api
