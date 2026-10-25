
package org.firstinspires.ftc.teamcode.opmodes.debug;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.NECTAR_MAX_SERVO_POSITION;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.POLLEN_MAX_SERVO_POSITION;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Config
@TeleOp
public class ShooterPractice extends OpMode {

    private DcMotorEx shooterMotor;
    private DcMotorEx shooterMotor2;
    private Servo hood1;
    private Servo hood2;

    private static final double TICKS_PER_REV = 28.0;
    private static final double NO_LOAD_RPM = 5800.0;

    public static double targetRPM = 3000.0;
    public static double kP = 0.00010;
    public static double kI = 0.00001;

    public static double hoodAngle1 = 0.5;
    public static double hoodAngle2 = 0.3;

    private double integral = 0.0;
    private long lastTimeNs;

    @Override
    public void init() {

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");

        hood1 = hardwareMap.get(Servo.class, "hood1");
        hood2 = hardwareMap.get(Servo.class, "hood2");

        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );
    }

    @Override
    public void start() {
        integral = 0.0;
        lastTimeNs = System.nanoTime();
    }

    @Override
    public void loop() {

        long now = System.nanoTime();

        double dt = (now - lastTimeNs) / 1_000_000_000.0;
        lastTimeNs = now;

        if (dt <= 0.0 || dt > 0.1) {
            dt = 0.02;
        }

        double target = Range.clip(targetRPM, 0.0, NO_LOAD_RPM);

        double measuredRPM =
                Math.abs(shooterMotor.getVelocity())
                        * 60.0 / TICKS_PER_REV;

        double error = target - measuredRPM;

        double feedForward = target / NO_LOAD_RPM;

        double motorPower = 0.0;

        if (target > 0.0) {

            double candidateIntegral = Range.clip(
                    integral + error * dt,
                    -5000.0,
                    5000.0
            );

            double candidatePower =
                    feedForward
                            + kP * error
                            + kI * candidateIntegral;

            if ((candidatePower >= 0.0 && candidatePower <= 1.0)
                    || (candidatePower > 1.0 && error < 0.0)
                    || (candidatePower < 0.0 && error > 0.0)) {

                integral = candidateIntegral;
            }

            motorPower = Range.clip(
                    feedForward + kP * error + kI * integral,
                    0.0,
                    1.0
            );

        } else {
            integral = 0.0;
        }

        shooterMotor.setPower(motorPower);
        shooterMotor2.setPower(-motorPower);

        hood1.setPosition(
                Range.clip(hoodAngle1, 0.0, NECTAR_MAX_SERVO_POSITION)
        );

        hood2.setPosition(
                Range.clip(hoodAngle2, 0.0, POLLEN_MAX_SERVO_POSITION)
        );

        telemetry.addData("Target RPM", "%.0f", target);
        telemetry.addData("Shared Shaft RPM", "%.0f", measuredRPM);
        telemetry.addData("RPM Error", "%+.0f", error);
        telemetry.addData("RPM Ready (+/- 30)",
                Math.abs(error) <= 30.0);
        telemetry.addData("Motor Power", "%.3f", motorPower);

        telemetry.addData("Hood 1 Position", hoodAngle1);
        telemetry.addData("Hood 2 Position", hoodAngle2);

        telemetry.addData("Raw Encoder Position OF SHOOTER 1",
                shooterMotor.getCurrentPosition());

        telemetry.addData("Raw Encoder Velocity (ticks/sec) OF SHOOTER 1",
                shooterMotor.getVelocity());

        telemetry.update();
    }

    @Override
    public void stop() {
        shooterMotor.setPower(0.0);
        shooterMotor2.setPower(0.0);
    }
}
