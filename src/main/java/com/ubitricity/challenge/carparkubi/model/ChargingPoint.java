package com.ubitricity.challenge.carparkubi.model;

import com.ubitricity.challenge.carparkubi.exception.ChargingPointAlreadyOccupiedException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@Log4j2
public class ChargingPoint {

    private final long id;
    private ChargingMode chargingMode;
    private ChargingPointStatus chargingPointStatus;
    private LocalDateTime plugInTime;
    private static final String EMPTY_SPACE = " ";

    public void plugged(ChargingMode chargingMode) throws ChargingPointAlreadyOccupiedException {
        if (ChargingPointStatus.OCCUPIED.equals(this.chargingPointStatus)) {
            throw new ChargingPointAlreadyOccupiedException();
        }

        this.chargingMode = chargingMode;
        this.chargingPointStatus = ChargingPointStatus.OCCUPIED;
        this.plugInTime = LocalDateTime.now();
    }

    public void unPlugged() {
        this.chargingMode = null;
        this.plugInTime = null;
        this.chargingPointStatus = ChargingPointStatus.AVAILABLE;
    }

    public String report() {
        StringBuilder report = new StringBuilder();
        report.append("CP").append(id).append(EMPTY_SPACE)
              .append(this.chargingPointStatus.name());

        if (ChargingPointStatus.OCCUPIED.equals(this.chargingPointStatus)) {
            report.append(EMPTY_SPACE + this.chargingMode.getCurrent()).append(EMPTY_SPACE + "A");
        }
        return report.toString();
    }
}