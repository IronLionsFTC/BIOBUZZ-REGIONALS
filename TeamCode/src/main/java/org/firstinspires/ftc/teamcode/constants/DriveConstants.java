package org.firstinspires.ftc.teamcode.constants;
import com.acmerobotics.dashboard.config.Config;

@Config
public class DriveConstants {

    // ======================
    //   Both bucket points
    // =======================

    public static double NORTH_TARGET_X = 10;
    public static double NORTH_TARGET_Y = 10;
    public static double SOUTH_TARGET_X = -1000;
    public static double SOUTH_TARGET_Y = -1000;


    // =============================
    //   Speed tuning for auto aim
    // =============================

    public static double KP_POWER = 0.2;
    public static double MAX_TURN_POWER = 0.7;
    public static double AIM_DEADBAND_DEGREES = 1.0;
    public static double KD_POWER = 0.5;
    public static double MIN_TURN_POWER = 0.5;




}
