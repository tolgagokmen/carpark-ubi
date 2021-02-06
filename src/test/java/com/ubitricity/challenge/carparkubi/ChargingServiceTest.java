package com.ubitricity.challenge.carparkubi;

import com.ubitricity.challenge.carparkubi.exception.CarParkNotExistException;
import com.ubitricity.challenge.carparkubi.exception.ChargingPointAlreadyOccupiedException;
import com.ubitricity.challenge.carparkubi.model.CarParkUbi;
import com.ubitricity.challenge.carparkubi.model.CarPlugInRequestModel;
import com.ubitricity.challenge.carparkubi.model.ChargingMode;
import com.ubitricity.challenge.carparkubi.model.ChargingPoint;
import com.ubitricity.challenge.carparkubi.service.ChargingService;
import java.util.concurrent.ConcurrentMap;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ChargingServiceTest {

    @Mock
    private ConcurrentMap<Long, ChargingPoint> chargingPoints;

    @Mock
    private CarParkUbi carparkUbi;

    @Mock
    private ConcurrentMap<Long, CarParkUbi> carParkUbiMap;

    @InjectMocks
    private ChargingService chargingService;

    @BeforeEach
    public void setValuesForFields() {
        ReflectionTestUtils.setField(chargingService, "carParkUbiMap", carParkUbiMap);
    }

    @Test
    public void testCarPluggedIn() throws CarParkNotExistException, ChargingPointAlreadyOccupiedException {
        CarPlugInRequestModel carPlugInRequestModel = ChargingPointControllerTest.createCarPlugInRequestModel();
        when(carParkUbiMap.get(anyLong())).thenReturn(carparkUbi);
        chargingService.carPluggedIn(carPlugInRequestModel.getCarparkUbiId(), carPlugInRequestModel
                .getConnectPointId(), carPlugInRequestModel.getChargingMode());

        verify(carparkUbi, atLeastOnce()).carPlugged(anyLong(),any(ChargingMode.class));
    }

    @Test
    public void testCarPluggedInThrowsChargingPointAlreadyOccupiedException() throws ChargingPointAlreadyOccupiedException {
        CarPlugInRequestModel carPlugInRequestModel = ChargingPointControllerTest.createCarPlugInRequestModel();
        when(carParkUbiMap.get(anyLong())).thenReturn(carparkUbi);
        doThrow(ChargingPointAlreadyOccupiedException.class).when(carparkUbi).carPlugged(anyLong(), any(ChargingMode.class));

        assertThrows(ChargingPointAlreadyOccupiedException.class, () -> chargingService
                .carPluggedIn(carPlugInRequestModel.getCarparkUbiId(), carPlugInRequestModel
                        .getConnectPointId(), carPlugInRequestModel.getChargingMode()));
    }

    @Test
    public void testCarUnplugged() throws CarParkNotExistException {
        when(carParkUbiMap.get(anyLong())).thenReturn(carparkUbi);
        chargingService.carUnPlugged(1l, 1l);

        verify(carparkUbi, atLeastOnce()).carUnplugged(anyLong());
    }

    @Test
    public void testCarUnpluggedThrowsCarParkNotExistException() {
        when(carParkUbiMap.get(anyLong())).thenReturn(null);
        assertThrows(CarParkNotExistException.class, () -> chargingService.carUnPlugged(1l, 1l));
    }

    @Test
    public void testgetCarParkUbiChargingPoints() throws CarParkNotExistException {
        ReflectionTestUtils.setField(carparkUbi, "chargingPoints", chargingPoints);
        when(carParkUbiMap.get(anyLong())).thenReturn(carparkUbi);
        when(carparkUbi.getChargingPoints()).thenReturn(chargingPoints);
        chargingService.getCarParkUbiChargingPoints(1l);

        verify(carparkUbi, atLeastOnce()).getChargingPoints();
    }

    @Test
    public void testGetCarParkUbiChargingPointsThrowsCarParkNotExistException() {
        when(carParkUbiMap.get(anyLong())).thenReturn(null);
        assertThrows(CarParkNotExistException.class, () -> chargingService.getCarParkUbiChargingPoints(1l));
    }
}
