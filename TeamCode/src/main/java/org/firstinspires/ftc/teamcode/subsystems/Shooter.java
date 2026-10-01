package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_KD;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_KI;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_KP;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;


public class Shooter {

    private static final String MOTOR_NAME_1 = "shooter1";
    private static final String MOTOR_NAME_2 = "shooter2";

    private static final boolean MOTOR_1_INVERTED = false;
    private static final boolean MOTOR_2_INVERTED = false;

    private static final double POWER_BAND = 0.02;

    private final MotorEx motor1;
    private final MotorEx motor2;

    private double targetPower = 0.0;


    public Shooter(HardwareMap hardwareMap) {

        motor1 = new MotorEx(
                hardwareMap,
                MOTOR_NAME_1
        );

        motor1.setInverted(MOTOR_1_INVERTED);
        motor1.setRunMode(Motor.RunMode.VelocityControl);

        motor1.setVeloCoefficients(
                SHOOTER_KP,
                SHOOTER_KI,
                SHOOTER_KD
        );

        motor1.setFeedforwardCoefficients(
                0.0,
                1.0 / motor1.ACHIEVABLE_MAX_TICKS_PER_SECOND,
                0.0
        );


        motor2 = new MotorEx(
                hardwareMap,
                MOTOR_NAME_2
        );

        motor2.setInverted(MOTOR_2_INVERTED);
        motor2.setRunMode(Motor.RunMode.VelocityControl);

        motor2.setVeloCoefficients(
                SHOOTER_KP,
                SHOOTER_KI,
                SHOOTER_KD
        );

        motor2.setFeedforwardCoefficients(
                0.0,
                1.0 / motor2.ACHIEVABLE_MAX_TICKS_PER_SECOND,
                0.0
        );
    }


    public void setTargetPower(double power) {

        targetPower = Range.clip(
                power,
                0.0,
                1.0
        );

        double targetVelocity1 =
                targetPower * motor1.ACHIEVABLE_MAX_TICKS_PER_SECOND;

        double targetVelocity2 =
                targetPower * motor2.ACHIEVABLE_MAX_TICKS_PER_SECOND;

        motor1.setVelocity(targetVelocity1);
        motor2.setVelocity(targetVelocity2);
    }


    public double getTargetPower() {

        return targetPower;
    }


    public double getCurrentPower() {

        double motor1Power =
                Math.abs(motor1.getVelocity())
                        / motor1.ACHIEVABLE_MAX_TICKS_PER_SECOND;

        double motor2Power =
                Math.abs(motor2.getVelocity())
                        / motor2.ACHIEVABLE_MAX_TICKS_PER_SECOND;

        return (
                motor1Power
                        + motor2Power
        ) / 2.0;
    }


    public boolean isAtTargetPower() {

        return Math.abs(
                getCurrentPower() - targetPower
        ) <= POWER_BAND;
    }


    public boolean isAtTargetPower(double tolerance) {

        return Math.abs(
                getCurrentPower() - targetPower
        ) <= tolerance;
    }


    public double getOutputPower() {

        return (
                Math.abs(motor1.getRawPower())
                        + Math.abs(motor2.getRawPower())
        ) / 2.0;
    }


    public void stop() {

        targetPower = 0.0;

        motor1.stopMotor();
        motor2.stopMotor();
    }
}