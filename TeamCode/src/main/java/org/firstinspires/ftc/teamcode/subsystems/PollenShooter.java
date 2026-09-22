package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.POLLEN_KD;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.POLLEN_KI;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.POLLEN_KP;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;


public class PollenShooter {

    private static final String motorName = "pollenShooter";

    private static final boolean motorInverted = false;


    // Allowed band for the shooter to actually shoot
    private static final double rpmBand = 50;


    private final MotorEx motor;

    private double targetRPM = 0.0;


    // =========================================================================================
    // SolversLib has a bunch of items to help with PIDFs and getting Motors RPM and setting it
    // =========================================================================================
    public PollenShooter(HardwareMap hardwareMap) {
        // Things that SolversLib needs
        motor = new MotorEx(hardwareMap, motorName);
        motor.setInverted(motorInverted);
        motor.setRunMode(Motor.RunMode.VelocityControl);

        // This is setting the PID (it can be tuned cos of the consants file)
        motor.setVeloCoefficients(
                POLLEN_KP,
                POLLEN_KI,
                POLLEN_KD
        );

        // Basic feedforward starting point
        motor.setFeedforwardCoefficients(
                0.0,
                1.0 / motor.ACHIEVABLE_MAX_TICKS_PER_SECOND,
                0.0
        );
    }


    // ============================================================================
    // This is where we set the TargetRPM, we give it an RPM from the lookup table
    // ============================================================================
    public void setTargetRPM(double rpm) {
        // Makes sure its positive, else its 0
        targetRPM = Math.max(0.0, rpm);

        // CPR is counts per revolution
        double ticksPerSecond = targetRPM * motor.getCPR() / 60.0;

        motor.setVelocity(ticksPerSecond);
    }


    public double getTargetRPM() {

        return targetRPM;
    }


    public double getCurrentRPM() {

        double motorRPM =
                motor.getVelocity()
                        * 60.0
                        / motor.getCPR();

        return Math.abs(
                motorRPM
        );
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

        return motor.getRawPower();
    }


    public void stop() {

        targetRPM = 0.0;
        motor.stopMotor();
    }
}