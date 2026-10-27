package org.firstinspires.ftc.teamcode.opmodes.debug;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.NECTAR_MAX_SERVO_POSITION;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.POLLEN_MAX_SERVO_POSITION;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Config
@TeleOp
public class ShooterPractice extends OpMode {

    private Shooter shooter;

    private Servo hood1;
    private Servo hood2;

    public static double targetRPM = 2000.0;

    public static double hoodAngle1 = 0.5;
    public static double hoodAngle2 = 0.3;

    @Override
    public void init() {

        shooter = new Shooter(hardwareMap);

        hood1 = hardwareMap.get(Servo.class, "hood1");
        hood2 = hardwareMap.get(Servo.class, "hood2");

        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );
    }

    @Override
    public void loop() {

        // Use the same controller as RedTeleOp and Auto.
        shooter.setTargetRPM(targetRPM);
        shooter.update();

        hood1.setPosition(
                Range.clip(
                        hoodAngle1,
                        0.0,
                        NECTAR_MAX_SERVO_POSITION
                )
        );

        hood2.setPosition(
                Range.clip(
                        hoodAngle2,
                        0.0,
                        POLLEN_MAX_SERVO_POSITION
                )
        );

        telemetry.addData("Target RPM", "%.0f", shooter.getTargetRPM());
        telemetry.addData("Measured RPM", "%.0f", shooter.getCurrentRPM());
        telemetry.addData("RPM Error", "%+.0f", shooter.getRPMError());

        telemetry.addData(
                "Motor Power",
                "%.3f",
                shooter.getOutputPower()
        );

        telemetry.addData("Shooter Ready", shooter.isReady());
        telemetry.addData(
                "Battery Voltage",
                "%.2f",
                shooter.getBatteryVoltage()
        );

        telemetry.addData(
                "Encoder Position",
                shooter.getEncoderPosition()
        );

        telemetry.addData(
                "Encoder Velocity (ticks/sec)",
                "%.1f",
                shooter.getEncoderVelocityTicksPerSecond()
        );

        telemetry.addData("Hood 1 Position", hoodAngle1);
        telemetry.addData("Hood 2 Position", hoodAngle2);

        telemetry.update();
    }

    @Override
    public void stop() {

        if (shooter != null) {
            shooter.stop();
        }
    }
}