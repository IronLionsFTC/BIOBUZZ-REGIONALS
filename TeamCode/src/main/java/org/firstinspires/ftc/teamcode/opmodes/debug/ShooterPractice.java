package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

@Config
@TeleOp
public class ShooterPractice extends OpMode {

    private DcMotorEx shooterMotor;
    private DcMotorEx shooterMotor2;
    private Servo hood1;
    private Servo hood2;

    public static double shooterPower = 0.0;

    // Set this to the encoder ticks per revolution of the motor.
    public static double tpr = 28.0;

    public static double shooterRPM = 0.0;
    public static double shooterRPM2 = 0.0;
    public static double hoodAngle1 = 0.0;
    public static double hoodAngle2 = 0.0;

    private PID motor1PID;
    private PID motor2PID;

    private long lastUpdateTime;

    @Override
    public void init() {

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        hood1 = hardwareMap.get(Servo.class, "hood1");
        hood2 = hardwareMap.get(Servo.class, "hood2");

        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1PID = new PID();
        motor2PID = new PID();

        lastUpdateTime = System.nanoTime();

        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );
    }

    @Override
    public void loop() {

        long currentTime = System.nanoTime();

        double dt = (currentTime - lastUpdateTime) / 1_000_000_000.0;

        lastUpdateTime = currentTime;

        if (dt <= 0.0 || dt > 0.1) {
            dt = 0.02;
        }

        double maxTicksPerSecond1 =
                shooterMotor.getMotorType().getAchieveableMaxTicksPerSecond();

        double maxTicksPerSecond2 =
                shooterMotor2.getMotorType().getAchieveableMaxTicksPerSecond();

        double targetVelocity1 = shooterPower * maxTicksPerSecond1;
        double targetVelocity2 = shooterPower * maxTicksPerSecond2;

        double currentVelocity1 = Math.abs(shooterMotor.getVelocity());
        double currentVelocity2 = Math.abs(shooterMotor2.getVelocity());

        double output1;
        double output2;

        if (shooterPower <= 0.0) {
            motor1PID.reset();
            motor2PID.reset();

            output1 = 0.0;
            output2 = 0.0;
        } else {
            output1 = motor1PID.calculate(
                    targetVelocity1,
                    currentVelocity1,
                    dt,
                    shooterPower
            );

            output2 = motor2PID.calculate(
                    targetVelocity2,
                    currentVelocity2,
                    dt,
                    shooterPower
            );
        }

        shooterMotor.setPower(-output1);
        shooterMotor2.setPower(output2);

        hood1.setPosition(Range.clip(hoodAngle1, 0.0, 1.0));
        hood2.setPosition(Range.clip(hoodAngle2, 0.0, 1.0));

        shooterRPM = currentVelocity1 * 60.0 / tpr;
        shooterRPM2 = currentVelocity2 * 60.0 / tpr;

        telemetry.addData("Shooter Power Target", "%.2f", shooterPower);
        telemetry.addData("Shooter 1 Output", "%.2f", output1);
        telemetry.addData("Shooter 2 Output", "%.2f", output2);

        telemetry.addData("Shooter 1 RPM", "%.0f", shooterRPM);
        telemetry.addData("Shooter 2 RPM", "%.0f", shooterRPM2);

        telemetry.addData("Hood1 Pos", hoodAngle1);
        telemetry.addData("Hood2 Pos", hoodAngle2);

        telemetry.update();
    }

    @Override
    public void stop() {

        shooterMotor.setPower(0.0);
        shooterMotor2.setPower(0.0);

        motor1PID.reset();
        motor2PID.reset();
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

            double error = targetVelocity - currentVelocity;

            double derivative = (error - lastError) / dt;

            double proportional =
                    ShooterConstants.SHOOTER_KP * error;

            double integralTerm =
                    ShooterConstants.SHOOTER_KI * integral;

            double derivativeTerm =
                    ShooterConstants.SHOOTER_KD * derivative;

            double correction =
                    proportional + integralTerm + derivativeTerm;

            double output = targetPower + correction;

            double clippedOutput = Range.clip(output, 0.0, 1.0);

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