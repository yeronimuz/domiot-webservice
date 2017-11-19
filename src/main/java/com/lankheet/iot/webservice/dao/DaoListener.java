package com.lankheet.iot.webservice.dao;

import java.util.List;
import com.lankheet.iot.datatypes.Measurement;

public interface DaoListener {
    /**
     * A new measurement has arrived.
     * 
     * @param measurement The measurement that has arrived.
     */
    void newMeasurement(Measurement measurement);

    /**
     * A client requests all measurements from the database
     * @return The measurements from the database.
     */
    List<Measurement> getMeasurements();

}
