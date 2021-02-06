package com.ubitricity.challenge.carparkubi.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.LongStream;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CarparkUbi {

    @Setter
    private long id;

    private long capacity;

    private ConcurrentMap<Long, ChargingPoint> chargingPoints = new ConcurrentHashMap<>();

    public CarparkUbi(long chargingPointCount) {
        this.capacity = ChargingMode.SLOW.getCurrent() * chargingPointCount;

        LongStream.rangeClosed(1, chargingPointCount).forEach(i -> {
            this.chargingPoints
                    .put(i, ChargingPoint.builder().id(i).chargingPointStatus(ChargingPointStatus.AVAILABLE).build());
        });
    }

    public void carPlugged(Long chargingPointId, ChargingMode chargingMode) {
        this.chargingPoints.get(chargingPointId).plugged(chargingMode);
        final long totalCurrent = this.chargingPoints.values()
                                                     .stream()
                                                     .filter(cp -> Objects.nonNull(cp.getChargingMode()))
                                                     .map(ChargingPoint::getChargingMode)
                                                     .mapToLong(ChargingMode::getCurrent)
                                                     .sum();

        if (totalCurrent > this.capacity) {
            long requiredDegradableChargingPoints = (totalCurrent - capacity) / ChargingMode.SLOW.getCurrent();

            chargingPoints.values()
                          .stream()
                          .filter(cp -> ChargingMode.FAST.equals(cp.getChargingMode()))
                          .sorted(Comparator.comparing(ChargingPoint::getPlugInTime))
                          .limit(requiredDegradableChargingPoints)
                          .forEach(cp -> cp.setChargingMode(ChargingMode.SLOW));
        }
    }

    public void carUnplugged(Long chargingPointId) {
        this.chargingPoints.get(chargingPointId).unPlugged();
    }

}
