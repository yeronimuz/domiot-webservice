package org.domiot.webservice.services;

import lombok.extern.slf4j.Slf4j;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This service works as a cache for retrieving SensorValue entities.
 * It's primary function is to cache retrieved data so that it can be returned fast the next time it is requested.
 * In addition, it will filter cached entries when a subset of the cached values is required.
 * <li>Multiple entries per sensorId are allowed</li>
 * <li>Unsampled values are stored.</li>
 */
@Service
@Slf4j
public class SensorValueCachingService {
    ConcurrentHashMap<SensorValueRequest, List<SensorValueEntity>> cache = new ConcurrentHashMap<>();

    public List<SensorValueEntity> getCachedValues(SensorValueRequest request) {
        if (cache.containsKey(request)) {
            return cache.get(request);
        }
        if (cacheContainsSuperSet(request)) {
            return cache.keySet().stream()
                    // Filter the cache entries
                    .filter(key -> Objects.equals(key.sensorId, request.getSensorId()))
                    .filter(key -> key.startTime.isBefore(request.getStartTime()))
                    .filter(key -> key.endTime.isAfter(request.getEndTime()))
                    .flatMap(key -> cache.get(key).stream())
                    // Filter the cache entry
                    .filter(sensorValueEntity -> sensorValueEntity.getTimeStamp().isAfter(request.startTime.minusNanos(1L)))
                    .filter(sensorValueEntity -> sensorValueEntity.getTimeStamp().isBefore(request.endTime.plusNanos(1L)))
                    .sorted(Comparator.comparing(SensorValueEntity::getTimeStamp))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Adds the given SensorValueEntity values to the cache using the provided SensorValueRequest as the key.
     * If a value already exists for the given sensor, it updates the cache only if the new value set contains values that would extend the cached ones.
     * Otherwise, if the new dataset is a subset of the cached one it does nothing and return false.
     *
     * @param request The SensorValueRequest object used as the key in the cache
     * @param values  The list of SensorValueEntity values to be added to the cache
     * @return true if the values were successfully added to the cache, false if a value already exists for the given request
     */
    public boolean addValues(SensorValueRequest request, List<SensorValueEntity> values) {
        if (cacheContainsSuperSet(request)) {
            return false;
        }

        return cache.putIfAbsent(request, values) == null;
    }

    private boolean cacheContainsSuperSet(SensorValueRequest request) {
        return cache.keySet().stream()
                .anyMatch(key -> Objects.equals(key.sensorId, request.getSensorId())
                        && key.getStartTime().isBefore(request.getStartTime())
                        && key.getEndTime().isAfter(request.getEndTime()));
    }
}
