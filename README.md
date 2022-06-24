# lnb-iot-webservice
This project used to serve the mqtt handling and storing of measurements in the database. This has been moved to the lnb-domotics-backend repository. It will be refactored to a web service that can handle all operational parameters of the domotics eco-system.
Features to be expected:
* Adding homes, rooms, sensors, users and permissions
* Serving measurement data in table and graphics (a separate project may be proper for this)
* Intelligence about switching statistics. It would be nice if we could determine what device is consuming how much power and when.
* Serving Configuration Data for IOT modules
* Cleanup algorithm. We don't want to store all measurements indefinitely. It would be nice if we could generate statistics, store them and remove the old measurements.
