package org.firstinspires.ftc.teamcode.constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ShooterConstants {

    // ===================
    // Shooter PID
    // ===================

    public static double SHOOTER_KP = 0.002;
    public static double SHOOTER_KD = 0.0;
    public static double SHOOTER_KI = 0.0;
    public static double TIME_TO_SHOOT = 1.5;

    // ===================
    // Pollen Hood Items
    // ===================
    public static final double MIN_HOOD_ANGLE = 0.0;
    public static final double MAX_HOOD_ANGLE = 30.0;

    public static final double POLLEN_MIN_SERVO_POSITION = 0.0;
    public static final double POLLEN_MAX_SERVO_POSITION = 0.7685;

    // ===================
    //  Nectar Hood Items
    // ===================

    public static final double NECTAR_MIN_SERVO_POSITION = 0.0;
    public static final double NECTAR_MAX_SERVO_POSITION = 0.75;

    // ===================
    //  Shooter Blocker
    // ===================

        public static double SHOOTER_BLOCKER_CLOSED_POSITION = 0.7;
    public static double SHOOTER_BLOCKER_OPEN_POSITION = 0.5;

    // ==========================
    // Intake and Transfer Power
    // ==========================

    public static double INTAKE_DEFAULT_POWER = 0.6;
    public static double TRANSFER_DEFAULT_POWER = 0.6;


}
