package org.firstinspires.ftc.teamcode.math;

import com.seattlesolvers.solverslib.util.InterpLUT;


public class ShooterLookupTables {

    private final InterpLUT pollenRPM = new InterpLUT();
    private final InterpLUT pollenHood = new InterpLUT();

    private final InterpLUT nectarRPM = new InterpLUT();
    private final InterpLUT nectarHood = new InterpLUT();


    public ShooterLookupTables() {

        // ========================================================
        // Pollen Look-Up Tables (TO BE REPLACED WITH REAL VALUES)
        // ========================================================

        pollenRPM.add(20, 3000);
        pollenRPM.add(30, 3200);
        pollenRPM.add(40, 3450);
        pollenRPM.add(50, 3700);
        pollenRPM.createLUT();


        pollenHood.add(0, 0);
        pollenHood.add(20, 7.5);
        pollenHood.add(40, 15);
        pollenHood.add(60, 22.5);
        pollenHood.add(80, 30);
        pollenHood.createLUT();


        // ========================================================
        // Nectar Look-Up Tables (TO BE REPLACED WITH REAL VALUES)
        // ========================================================

        nectarRPM.add(20, 2800);
        nectarRPM.add(30, 3000);
        nectarRPM.add(40, 3250);
        nectarRPM.add(50, 3500);
        nectarRPM.createLUT();


        nectarHood.add(20, 0.28);
        nectarHood.add(30, 0.33);
        nectarHood.add(40, 0.38);
        nectarHood.add(50, 0.43);
        nectarHood.createLUT();
    }


    // ================================
    // Simple way to read the table
    // ================================

    public double getPollenRPM(double distance) {
        return pollenRPM.get(distance);
    }

    public double getPollenHood(double distance) {
        return pollenHood.get(distance);
    }

    public double getNectarRPM(double distance) {
        return nectarRPM.get(distance);
    }

    public double getNectarHood(double distance) {
        return nectarHood.get(distance);
    }
}