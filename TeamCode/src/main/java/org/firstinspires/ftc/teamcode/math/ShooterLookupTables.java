package org.firstinspires.ftc.teamcode.math;

import com.seattlesolvers.solverslib.util.InterpLUT;


public class ShooterLookupTables {

    private final InterpLUT shooterRPM = new InterpLUT();
    private final InterpLUT pollenHood = new InterpLUT();

    private final InterpLUT nectarHood = new InterpLUT();


    public ShooterLookupTables() {

        // ========================================================
        // Pollen Look-Up Tables (TO BE REPLACED WITH REAL VALUES)
        // ========================================================

        shooterRPM.add(20, 0.0);
        shooterRPM.add(30, 0.2);
        shooterRPM.add(40, 0.4);
        shooterRPM.add(50, 0.7);
        shooterRPM.createLUT();


        pollenHood.add(0, 0);
        pollenHood.add(20, 7.5);
        pollenHood.add(40, 15);
        pollenHood.add(60, 22.5);
        pollenHood.add(80, 30);
        pollenHood.createLUT();


        // ========================================================
        // Nectar Look-Up Tables (TO BE REPLACED WITH REAL VALUES)
        // ========================================================

        nectarHood.add(0, 0);
        nectarHood.add(20, 7.5);
        nectarHood.add(40, 15);
        nectarHood.add(60, 22.5);
        nectarHood.add(80, 30);
        nectarHood.createLUT();
    }


    // ================================
    // Simple way to read the table
    // ================================

    public double getShooterRPM(double distance) {
        return shooterRPM.get(distance);
    }
    public double getPollenHood(double distance) {return pollenHood.get(distance);}
    public double getNectarHood(double distance) {
        return nectarHood.get(distance);
    }
}