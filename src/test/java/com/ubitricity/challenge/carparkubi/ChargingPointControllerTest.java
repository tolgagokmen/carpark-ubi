package com.ubitricity.challenge.carparkubi;

import com.ubitricity.challenge.carparkubi.controller.ChargingPointController;
import com.ubitricity.challenge.carparkubi.exception.CarParkNotExistException;
import com.ubitricity.challenge.carparkubi.exception.ChargingPointAlreadyOccupiedException;
import com.ubitricity.challenge.carparkubi.exception.GlobalExceptionHandler;
import com.ubitricity.challenge.carparkubi.model.CarPlugInRequestModel;
import com.ubitricity.challenge.carparkubi.model.ChargingMode;
import com.ubitricity.challenge.carparkubi.service.ChargingService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class ChargingPointControllerTest {

    @Mock
    private ChargingService chargingService;

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new ChargingPointController(chargingService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    }

    @Test
    public void testPostCarPlugInRequestModel() throws Exception {
        CarPlugInRequestModel carPlugInRequestModel = createCarPlugInRequestModel();
        this.mockMvc.perform(post("/plugin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json(carPlugInRequestModel)))
                    .andExpect(status().isOk());
    }

    @Test
    public void testPostCarPlugInThrowsCarParkNotExistException() throws Exception {
        CarPlugInRequestModel carPlugInRequestModel = createCarPlugInRequestModel();
        doThrow(CarParkNotExistException.class).when(chargingService).carPluggedIn(anyLong(),anyLong(),any());
        this.mockMvc.perform(post("/plugin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json(carPlugInRequestModel)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostCarPlugInThrowsChargingPointAlreadyOccupiedException() throws Exception {
        CarPlugInRequestModel carPlugInRequestModel = createCarPlugInRequestModel();
        doThrow(ChargingPointAlreadyOccupiedException.class).when(chargingService).carPluggedIn(anyLong(),anyLong(),any());
        this.mockMvc.perform(post("/plugin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json(carPlugInRequestModel)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostCarPlugInThrowsRuntimeException() throws Exception {
        CarPlugInRequestModel carPlugInRequestModel = createCarPlugInRequestModel();
        doThrow(RuntimeException.class).when(chargingService).carPluggedIn(anyLong(),anyLong(),any());
        this.mockMvc.perform(post("/plugin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json(carPlugInRequestModel)))
                    .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteUnPlugCarEntry() throws Exception {
        this.mockMvc.perform(delete("/unplug/1/1")).andExpect(status().isOk());
    }

    @Test
    public void testDeleteEntryThrowsBadRequestException() throws Exception {
        doThrow(CarParkNotExistException.class).when(chargingService).carUnPlugged(anyLong(), anyLong());
        this.mockMvc.perform(delete("/unplug/1/1")).andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteEntryThrowsRuntimeException() throws Exception {
        doThrow(RuntimeException.class).when(chargingService).carUnPlugged(anyLong(), anyLong());
        this.mockMvc.perform(delete("/unplug/1/1")).andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetReport() throws Exception {
        this.mockMvc.perform(get("/report/1")).andExpect(status().isOk());
    }

    @Test
    public void testGetReportThrowsCarParkNotExistException() throws Exception {
        doThrow(CarParkNotExistException.class).when(chargingService).getCarParkUbiChargingPoints(anyLong());
        this.mockMvc.perform(get("/report/1")).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetReportThrowsRuntimeException() throws Exception {
        doThrow(RuntimeException.class).when(chargingService).getCarParkUbiChargingPoints(anyLong());
        this.mockMvc.perform(get("/report/1")).andExpect(status().isInternalServerError());
    }

    public static CarPlugInRequestModel createCarPlugInRequestModel() {
        CarPlugInRequestModel carPlugInRequestModel = new CarPlugInRequestModel();
        carPlugInRequestModel.setChargingMode(ChargingMode.FAST);
        carPlugInRequestModel.setCarparkUbiId(1l);
        carPlugInRequestModel.setConnectPointId(1l);
        return carPlugInRequestModel;
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
