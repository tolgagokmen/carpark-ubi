package com.ubitricity.challenge.carparkubi.service;

import com.ubitricity.challenge.carparkubi.exception.CarParkNotExistException;
import com.ubitricity.challenge.carparkubi.exception.ChargingPointAlreadyOccupiedException;
import com.ubitricity.challenge.carparkubi.model.CarParkUbi;
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

    private ConcurrentMap<Long, CarParkUbi> carParkUbiMap = new ConcurrentHashMap<>();

    @Value("${carparkubi.count}")
    private Long carparkUbiCount;

    @PostConstruct
    public void init() {
        LongStream.rangeClosed(1, this.carparkUbiCount)
                  .forEach(i -> {
                      CarParkUbi carparkUbi = new CarParkUbi(10);
                      carparkUbi.setId(i);
                      this.carParkUbiMap.put(i, carparkUbi);
                  });
    }

    public void carPluggedIn(Long carparkUbiId, Long chargingPointId, ChargingMode chargingMode) throws ChargingPointAlreadyOccupiedException, CarParkNotExistException {
        log.debug("Plugging car to charging point {} on carparkUbi {} with charging mode {} attempted");
        CarParkUbi carparkUbi = carParkUbiMap.get(carparkUbiId);
        if(carparkUbi == null){
            throw new CarParkNotExistException();
        }
        carparkUbi.carPlugged(chargingPointId, chargingMode);
    }

    public void carUnPlugged(Long carparkUbiId, Long chargingPointId) throws CarParkNotExistException {
        log.debug("Car unplugged from charging point {} on carparkUbi {}");
        CarParkUbi carparkUbi = carParkUbiMap.get(carparkUbiId);
        if(carparkUbi == null){
            throw new CarParkNotExistException();
        }
        carparkUbi.carUnplugged(chargingPointId);
    }

    public Collection<ChargingPoint> getCarParkUbiChargingPoints(Long carparkUbiId) throws CarParkNotExistException {
        CarParkUbi carparkUbi = carParkUbiMap.get(carparkUbiId);
        if(carparkUbi == null){
            throw new CarParkNotExistException();
        }
        return carparkUbi.getChargingPoints().values();
    }

}
