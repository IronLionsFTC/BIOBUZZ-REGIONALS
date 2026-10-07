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

    private final PID motor1PID = new PID();
    private final PID motor2PID = new PID();

    private double targetPower = 0.0;

    private long lastUpdateTime;

    public Shooter(HardwareMap hardwareMap) {

        motor1 = new MotorEx(
                hardwareMap,
                MOTOR_NAME_1
        );

        motor1.setInverted(MOTOR_1_INVERTED);
        motor1.setRunMode(Motor.RunMode.RawPower);


        motor2 = new MotorEx(
                hardwareMap,
                MOTOR_NAME_2
        );

        motor2.setInverted(MOTOR_2_INVERTED);
        motor2.setRunMode(Motor.RunMode.RawPower);

        lastUpdateTime = System.nanoTime();
    }


    public void setTargetPower(double power) {

        targetPower = Range.clip(
                power,
                0.0,
                1.0
        );
    }


    public void update() {

        long currentTime = System.nanoTime();

        double dt = (currentTime - lastUpdateTime) / 1_000_000_000.0;

        lastUpdateTime = currentTime;

        if (dt <= 0.0 || dt > 0.1) {
            dt = 0.02;
        }

        double targetVelocity1 =
                targetPower * motor1.ACHIEVABLE_MAX_TICKS_PER_SECOND;

        double targetVelocity2 =
                targetPower * motor2.ACHIEVABLE_MAX_TICKS_PER_SECOND;

        double currentVelocity1 =
                Math.abs(motor1.getVelocity());

        double currentVelocity2 =
                Math.abs(motor2.getVelocity());

        double output1 = motor1PID.calculate(
                targetVelocity1,
                currentVelocity1,
                dt,
                targetPower
        );

        double output2 = motor2PID.calculate(
                targetVelocity2,
                currentVelocity2,
                dt,
                targetPower
        );

        motor1.set(output1);
        motor2.set(output2);
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

        motor1PID.reset();
        motor2PID.reset();

        motor1.stopMotor();
        motor2.stopMotor();

        lastUpdateTime = System.nanoTime();
    }


    private static class PID {

        private double integral = 0.0;
        private double lastError = 0.0;

        public double calculate(
                double targetVelocity,
                double currentVelocity,
                double dt,
                double targetPower
        ) {

            double error =
                    targetVelocity - currentVelocity;


            double derivative =
                    (error - lastError) / dt;


            double proportional =
                    SHOOTER_KP * error;

            double integralTerm =
                    SHOOTER_KI * integral;

            double derivativeTerm =
                    SHOOTER_KD * derivative;


            double correction =
                    proportional
                            + integralTerm
                            + derivativeTerm;


            double output =
                    targetPower + correction;


            double clippedOutput =
                    Range.clip(
                            output,
                            0.0,
                            1.0
                    );


            if (output == clippedOutput) {
                integral += error * dt;
            }


            lastError = error;

            return clippedOutput;
        }


        public void reset() {

            integral = 0.0;
            lastError = 0.0;
        }
    }
}