package com.ubitricity.challenge.carparkubi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChargingMode {

    SLOW(10),
    FAST(20);

    private int current;
}
