package com.lankheet.iot.webservice;

import com.lankheet.iot.datatypes.Measurement;

public interface DaoListener {
	
	void newMeasurement(Measurement measurement);

}
