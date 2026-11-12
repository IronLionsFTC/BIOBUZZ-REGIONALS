package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.util.InterpLUT;

@Config
public class ShooterLookupTables {

    private final InterpLUT shooterRPM = new InterpLUT();
    private final InterpLUT pollenHood = new InterpLUT();

    private final InterpLUT nectarHood = new InterpLUT();


    public ShooterLookupTables() {

        // ========================================================
        // Pollen Look-Up Tables (TO BE REPLACED WITH REAL VALUES)
        // ========================================================

        shooterRPM.add(20, 2900);
        shooterRPM.add(30, 3000);
        shooterRPM.add(40, 3100);
        shooterRPM.add(50, 3200);
        shooterRPM.add(60, 3300);
        shooterRPM.createLUT();


        pollenHood.add(20, 0);
        pollenHood.add(30, 10);
        pollenHood.add(40, 15);
        pollenHood.add(50, 18);
        pollenHood.add(60, 20);
        pollenHood.createLUT();


        // ========================================================
        // Nectar Look-Up Tables (TO BE REPLACED WITH REAL VALUES)
        // ========================================================

        nectarHood.add(0, 0);
        nectarHood.add(20, 0);
        nectarHood.add(40, 0);
        nectarHood.add(60, 0);
        nectarHood.add(80, 0);
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