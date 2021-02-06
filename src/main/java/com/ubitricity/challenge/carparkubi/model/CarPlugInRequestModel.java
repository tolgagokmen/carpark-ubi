package com.ubitricity.challenge.carparkubi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarPlugInRequestModel {
    private long carparkUbiId;
    private long connectPointId;

    @NonNull
    private ChargingMode chargingMode;
}
