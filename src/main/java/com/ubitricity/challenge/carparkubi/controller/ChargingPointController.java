package com.ubitricity.challenge.carparkubi.controller;

import com.ubitricity.challenge.carparkubi.exception.BadRequestException;
import com.ubitricity.challenge.carparkubi.exception.CarParkNotExistException;
import com.ubitricity.challenge.carparkubi.exception.ChargingPointAlreadyOccupiedException;
import com.ubitricity.challenge.carparkubi.exception.InternalServerErrorException;
import com.ubitricity.challenge.carparkubi.exception.ServiceResponseException;
import com.ubitricity.challenge.carparkubi.model.CarPlugInRequestModel;
import com.ubitricity.challenge.carparkubi.model.ChargingPoint;
import com.ubitricity.challenge.carparkubi.service.ChargingService;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class ChargingPointController {

    private final ChargingService chargingService;

    public ChargingPointController(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    @PostMapping("/plugin")
    public void plugInCar(@RequestBody CarPlugInRequestModel requestModel) throws ServiceResponseException {
        try {
            this.chargingService.carPluggedIn(
                    requestModel.getCarparkUbiId(),
                    requestModel.getConnectPointId(),
                    requestModel.getChargingMode()
            );
        } catch (ChargingPointAlreadyOccupiedException chargingPointAlreadyOccupiedException) {
            log.error("Charging point is already occupied: {} {}", requestModel, chargingPointAlreadyOccupiedException
                    .getStackTrace());
            throw new BadRequestException("Charging point is already occupied", chargingPointAlreadyOccupiedException);
        } catch (CarParkNotExistException carParkNotExistException) {
            log.error("CarPark Not Exist: {} {}", requestModel, carParkNotExistException.getStackTrace());
            throw new BadRequestException("CarPark Not Exist", carParkNotExistException);
        } catch (Exception exception) {
            log.error("Could not plug in {} {}", requestModel, exception.getStackTrace());
            throw new InternalServerErrorException(exception.getMessage());
        }
    }

    @DeleteMapping("/unplug/{carparkUbiId}/{chargingPointId}")
    public void unplugCar(@PathVariable("carparkUbiId") Long carparkUbiId, @PathVariable("chargingPointId") Long chargingPointId) throws ServiceResponseException {
        try {
            this.chargingService.carUnPlugged(carparkUbiId, chargingPointId);
        } catch (CarParkNotExistException carParkNotExistException) {
            log.error("CarPark Not Exist for given carparkUbiId: {} {}", carparkUbiId, carParkNotExistException
                    .getStackTrace());
            throw new BadRequestException("CarPark Not Exist", carParkNotExistException);
        } catch (Exception exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }
    }

    @GetMapping("/report/{carparkUbiId}")
    public String report(@PathVariable("carparkUbiId") Long carparkUbiId) throws ServiceResponseException {
        try {
            final Collection<ChargingPoint> chargingPoints = this.chargingService
                    .getCarParkUbiChargingPoints(carparkUbiId);
            String reportDetails = chargingPoints.stream()
                                                 .map(ChargingPoint::report)
                                                 .collect(Collectors
                                                         .joining("\n"));
            return String.format("==Report for carparkUid %s==\n%s", carparkUbiId, reportDetails);
        } catch (CarParkNotExistException carParkNotExistException) {
            log.error("CarPark Not Exist for given carparkUbiId: {} {}", carparkUbiId, carParkNotExistException
                    .getStackTrace());
            throw new BadRequestException("CarPark Not Exist", carParkNotExistException);
        } catch (Exception exception) {
            throw new InternalServerErrorException(exception.getMessage());
        }
    }
}
