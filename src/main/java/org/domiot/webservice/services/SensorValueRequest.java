package org.domiot.webservice.services;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The SensorValueRequest is an object that will be used as key in the hashmap that is used as cache for the sensorvalue resource.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorValueRequest {

    Integer sensorId;
    LocalDateTime startTime;
    LocalDateTime endTime;

    /**
     * The timestamp will contain the time of the request. Using this field, obsolete data can be removed.
     */
    @Transient
    LocalDateTime timestamp;
}
