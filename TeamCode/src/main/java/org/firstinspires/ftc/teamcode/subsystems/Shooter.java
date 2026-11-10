/// SHOOTER CODE IDEAS
///  We want: --> balls to be able to rapid fire no drop.
/// This likely means a _____ system (PID?)
/// We want this to be as good as possible
/// When a ball goes through it, and it drops, get back to speed as fast as possible
///
/// Need to:
/// --> Get the target RPM
/// --> Update the target RPM to maintain it
///
/// Mechanically it is quite weak. Thus, the code has to be spot on perfect.
/// We need simple and very reliable,
///
/// look at inspo
///
///
///
///



package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

@Config
public class Shooter {

    private static final String MOTOR_NAME_1 = "shooter1";
    private static final String MOTOR_NAME_2 = "shooter2";

    public static double TICKS_PER_REV = 28.0;
    public static double NO_LOAD_RPM = 5800.0;
    public static double NOMINAL_VOLTAGE = 12.0;

    public static double kF = 1.0;
    public static double kP = 0.00020;
    public static double kI = 0.0;

    public static double MAX_INTEGRAL = 5000.0;

    public static double READY_TOLERANCE_RPM = 75.0;
    public static double READY_STABLE_MS = 150.0;

    private static final double POWER_BAND = 0.02;

    private final DcMotorEx motor1;
    private final DcMotorEx motor2;
    private final DcMotorEx encoderMotor;
    private final Iterable<VoltageSensor> voltageSensors;

    private double targetRPM = 0.0;
    private double integral = 0.0;
    private double lastMotorPower = 0.0;

    private long lastUpdateTimeNs;
    private long readySinceNs = 0;

    public Shooter(HardwareMap hardwareMap) {

        motor1 = hardwareMap.get(
                DcMotorEx.class,
                MOTOR_NAME_1
        );

        motor2 = hardwareMap.get(
                DcMotorEx.class,
                MOTOR_NAME_2
        );

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setPower(0.0);
        motor2.setPower(0.0);

        // Change this to motor1 only if the encoder is connected to shooter1.
        encoderMotor = motor2;

        voltageSensors = hardwareMap.voltageSensor;

        lastUpdateTimeNs = System.nanoTime();
    }

    public void setTargetRPM(double rpm) {

        if (Double.isNaN(rpm) || Double.isInfinite(rpm)) {
            rpm = 0.0;
        }

        double maxRPM = Math.max(1.0, NO_LOAD_RPM);

        double newTarget = Range.clip(
                rpm,
                0.0,
                maxRPM
        );

        double targetChange = Math.max(
                25.0,
                Math.abs(READY_TOLERANCE_RPM)
        );

        if (Math.abs(newTarget - targetRPM) > targetChange) {
            readySinceNs = 0;
        }

        targetRPM = newTarget;

        if (targetRPM <= 0.0) {
            integral = 0.0;
            readySinceNs = 0;
        }
    }

    public void update() {

        long now = System.nanoTime();

        double dt = (now - lastUpdateTimeNs) / 1_000_000_000.0;
        lastUpdateTimeNs = now;

        if (dt <= 0.0 || dt > 0.1) {
            dt = 0.02;
        }

        double target = targetRPM;
        double measuredRPM = getCurrentRPM();
        double error = target - measuredRPM;

        double motorPower = 0.0;

        if (target > 0.0) {

            double maxRPM = Math.max(1.0, NO_LOAD_RPM);
            double nominalVoltage = Math.max(1.0, NOMINAL_VOLTAGE);
            double batteryVoltage = Math.max(6.0, getBatteryVoltage());

            double feedForward =
                    kF
                            * (target / maxRPM)
                            * (nominalVoltage / batteryVoltage);

            double integralLimit = Math.abs(MAX_INTEGRAL);

            double candidateIntegral = Range.clip(
                    integral + error * dt,
                    -integralLimit,
                    integralLimit
            );

            double candidatePower =
                    feedForward
                            + kP * error
                            + kI * candidateIntegral;

            // Prevent integral windup when the motor output is saturated.
            if ((candidatePower >= 0.0 && candidatePower <= 1.0)
                    || (candidatePower > 1.0 && error < 0.0)
                    || (candidatePower < 0.0 && error > 0.0)) {

                integral = candidateIntegral;
            }

            motorPower = Range.clip(
                    feedForward
                            + kP * error
                            + kI * integral,
                    0.0,
                    1.0
            );

        } else {

            integral = 0.0;
            readySinceNs = 0;
        }

        lastMotorPower = motorPower;

        // The two motors rotate in opposite electrical directions.
        // Their power magnitudes are controlled by one shared feedback loop.
        motor1.setPower(motorPower);
        motor2.setPower(-motorPower);

        // Readiness requires a continuous period inside the RPM tolerance.
        if (target > 0.0
                && Math.abs(error) <= Math.max(0.0, READY_TOLERANCE_RPM)) {

            if (readySinceNs == 0) {
                readySinceNs = now;
            }

        } else {

            readySinceNs = 0;
        }
    }

    public double getTargetRPM() {
        return targetRPM;
    }

    public double getCurrentRPM() {

        double ticksPerRev = Math.max(1.0, TICKS_PER_REV);

        return Math.abs(encoderMotor.getVelocity())
                * 60.0 / ticksPerRev;
    }

    public double getRPMError() {
        return targetRPM - getCurrentRPM();
    }

    public double getEncoderVelocityTicksPerSecond() {
        return encoderMotor.getVelocity();
    }

    public int getEncoderPosition() {
        return encoderMotor.getCurrentPosition();
    }

    public boolean isReady() {

        if (targetRPM <= 0.0 || readySinceNs == 0) {
            return false;
        }

        long stableTimeNs =
                (long) (Math.max(0.0, READY_STABLE_MS) * 1_000_000.0);

        return System.nanoTime() - readySinceNs >= stableTimeNs
                && Math.abs(getRPMError())
                <= Math.max(0.0, READY_TOLERANCE_RPM);
    }

    public double getOutputPower() {
        return lastMotorPower;
    }

    public double getBatteryVoltage() {

        double lowestVoltage = Double.POSITIVE_INFINITY;

        for (VoltageSensor sensor : voltageSensors) {

            double voltage = sensor.getVoltage();

            if (voltage > 1.0 && voltage < lowestVoltage) {
                lowestVoltage = voltage;
            }
        }

        if (Double.isInfinite(lowestVoltage)) {
            return NOMINAL_VOLTAGE;
        }

        return lowestVoltage;
    }

    // Compatibility methods for existing code that uses normalized power.

    public void setTargetPower(double power) {

        double maxRPM = Math.max(1.0, NO_LOAD_RPM);

        setTargetRPM(
                Range.clip(power, 0.0, 1.0) * maxRPM
        );
    }

    public double getTargetPower() {

        return targetRPM / Math.max(1.0, NO_LOAD_RPM);
    }

    public double getCurrentPower() {

        return Range.clip(
                getCurrentRPM() / Math.max(1.0, NO_LOAD_RPM),
                0.0,
                1.0
        );
    }

    public boolean isAtTargetPower() {
        return isAtTargetPower(POWER_BAND);
    }

    public boolean isAtTargetPower(double tolerance) {

        return Math.abs(
                getCurrentPower() - getTargetPower()
        ) <= tolerance;
    }

    public void stop() {

        targetRPM = 0.0;
        integral = 0.0;
        lastMotorPower = 0.0;
        readySinceNs = 0;

        motor1.setPower(0.0);
        motor2.setPower(0.0);

        lastUpdateTimeNs = System.nanoTime();
    }
}