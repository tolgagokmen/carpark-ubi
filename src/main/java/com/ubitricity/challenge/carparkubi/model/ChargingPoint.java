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

    private long id;
    private ChargingMode chargingMode;
    private ChargingPointStatus chargingPointStatus;
    private LocalDateTime plugInTime;

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
        report.append("CP").append(id).append(" ")
              .append(this.chargingPointStatus.name());

        if (ChargingPointStatus.OCCUPIED.equals(this.chargingPointStatus)) {
            report.append(this.chargingMode.getCurrent()).append("A");
        }
        return report.toString();
    }
}