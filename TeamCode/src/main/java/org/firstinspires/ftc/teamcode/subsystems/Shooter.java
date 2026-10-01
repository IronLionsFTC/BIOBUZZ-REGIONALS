package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_KD;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_KI;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_KP;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;


public class Shooter {

    private static final String motorName1 = "motor1";
    private static final String motorName2 = "motor2";

    private static final boolean motor1Inverted = false;
    private static final boolean motor2Inverted = false;


    // Allowed band for the shooter to actually shoot
    private static final double rpmBand = 50;


    private final MotorEx motor1;
    private final MotorEx motor2;

    private double targetRPM = 0.0;


    // =========================================================================================
    // SolversLib has a bunch of items to help with PIDFs and getting Motors RPM and setting it
    // =========================================================================================
    public Shooter(HardwareMap hardwareMap) {
        // Things that SolversLib needs
        motor1 = new MotorEx(hardwareMap, motorName1);
        motor1.setInverted(motor1Inverted);
        motor1.setRunMode(Motor.RunMode.VelocityControl);

        // This is setting the PID (it can be tuned cos of the consants file)
        motor1.setVeloCoefficients(
                SHOOTER_KP,
                SHOOTER_KI,
                SHOOTER_KD
        );

        // Basic feedforward starting point
        motor1.setFeedforwardCoefficients(
                0.0,
                1.0 / motor1.ACHIEVABLE_MAX_TICKS_PER_SECOND,
                0.0
        );


        motor2 = new MotorEx(hardwareMap, motorName2);
        motor2.setInverted(motor2Inverted);
        motor2.setRunMode(Motor.RunMode.VelocityControl);

        // This is setting the PID (it can be tuned cos of the consants file)
        motor2.setVeloCoefficients(
                SHOOTER_KP,
                SHOOTER_KI,
                SHOOTER_KD
        );

        // Basic feedforward starting point
        motor2.setFeedforwardCoefficients(0.0, 1.0 / motor2.ACHIEVABLE_MAX_TICKS_PER_SECOND, 0.0);
    }


    // ============================================================================
    // This is where we set the TargetRPM, we give it an RPM from the lookup table
    // ============================================================================
    public void setTargetRPM(double rpm) {
        // Makes sure its positive, else its 0
        targetRPM = Math.max(0.0, rpm);

        // CPR is counts per revolution
        double ticksPerSecond1 = targetRPM * motor1.getCPR() / 60.0;
        double ticksPerSecond2 = targetRPM * motor2.getCPR() / 60.0;

        motor1.setVelocity(ticksPerSecond1);
        motor2.setVelocity(ticksPerSecond2);
    }


    public double getTargetRPM() {

        return targetRPM;
    }


    public double getCurrentRPM() {

        double motor1RPM = motor1.getVelocity() * 60.0 / motor1.getCPR();
        double motor2RPM = motor2.getVelocity() * 60.0 / motor2.getCPR();

        return (Math.abs(motor1RPM) + Math.abs(motor2RPM)) / 2.0;
    }


    public boolean isAtTargetRPM() {

        return Math.abs(
                getCurrentRPM() - targetRPM
        ) <= rpmBand;
    }


    public boolean isAtTargetRPM(double tolerance) {

        return Math.abs(
                getCurrentRPM() - targetRPM
        ) <= tolerance;
    }


    public double getPower() {
        return (Math.abs(motor1.getRawPower()) + Math.abs(motor2.getRawPower())) / 2.0;
    }


    public void stop() {
        targetRPM = 0.0;
        motor1.stopMotor();
        motor2.stopMotor();
    }
}

