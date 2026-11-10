package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;
import com.seattlesolvers.solverslib.util.InterpLUT;

@Config
public class ShooterLookupTables {

    private final InterpLUT shooterRPM = new InterpLUT();
    private final InterpLUT pollenHood = new InterpLUT();

    private final InterpLUT nectarHood = new InterpLUT();
    public static double TWENTY_INCH_RPM = 2800;
    public static double THIRTY_INCH_RPM = 3000;
    public static double FOURTY_INCH_RPM = 3200;
    public static double FIFTY_INCH_RPM = 3400;
    public static double SIXTY_INCH_RPN = 3600;
    public static double SEVENTY_INCH_RPM = 3800;

    public static double ZERO_INCH_HOOD = 0;
    public static double TWENTY_INCH_HOOD = 5;
    public static double FOURTY_INCH_HOOD = 10;
    public static double SIXTY_INCH_HOOD = 15;

    public ShooterLookupTables() {

        // ========================================================
        // Pollen Look-Up Tables (TO BE REPLACED WITH REAL VALUES)
        // ========================================================

        shooterRPM.add(20, TWENTY_INCH_RPM);
        shooterRPM.add(30, THIRTY_INCH_RPM);
        shooterRPM.add(40, FOURTY_INCH_RPM);
        shooterRPM.add(50, FIFTY_INCH_RPM);
        shooterRPM.add(60, SIXTY_INCH_RPN);
        shooterRPM.add(70,SEVENTY_INCH_RPM);
        shooterRPM.createLUT();


        pollenHood.add(0, ZERO_INCH_HOOD);
        pollenHood.add(20,TWENTY_INCH_HOOD);
        pollenHood.add(40, FOURTY_INCH_HOOD);
        pollenHood.add(60, SIXTY_INCH_HOOD);
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