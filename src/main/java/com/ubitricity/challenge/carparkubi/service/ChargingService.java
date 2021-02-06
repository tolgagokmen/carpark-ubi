package com.ubitricity.challenge.carparkubi.service;

import com.ubitricity.challenge.carparkubi.model.CarparkUbi;
import com.ubitricity.challenge.carparkubi.model.ChargingMode;
import com.ubitricity.challenge.carparkubi.model.ChargingPoint;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.LongStream;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ChargingService {

    private ConcurrentMap<Long, CarparkUbi> carParkUbiMap = new ConcurrentHashMap<>();

    @Value("${carparkubi.count}")
    private Long carparkUbiCount;

    @PostConstruct
    public void init() {
        LongStream.range(1, this.carparkUbiCount)
                  .forEach(i -> {
                      CarparkUbi carparkUbi = new CarparkUbi(10);
                      carparkUbi.setId(i);

                      this.carParkUbiMap.put(i, carparkUbi);
                  });
    }

    public void carPluggedIn(Long carparkUbiId, Long chargingPointId, ChargingMode chargingMode) {
        log.debug("Plugging car to charging point {} on carparkUbi {} with charging mode {} attempted");
        this.carParkUbiMap.get(carparkUbiId).carPlugged(chargingPointId, chargingMode);
    }

    public void carUnPlugged(Long carparkUbiId, Long chargingPointId) {
        log.debug("Car unplugged from charging point {} on carparkUbi {}");
        this.carParkUbiMap.get(carparkUbiId).carUnplugged(chargingPointId);
    }

    public Collection<ChargingPoint> getCarparkUbiChargingPoints(Long carparkUbiId) {
        return this.carParkUbiMap.get(carparkUbiId).getChargingPoints().values();
    }

}
