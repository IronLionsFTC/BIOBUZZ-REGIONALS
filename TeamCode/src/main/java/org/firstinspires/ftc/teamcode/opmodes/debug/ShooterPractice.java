package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class ShooterPractice extends OpMode {

    private DcMotorEx shooterMotor;
    private DcMotorEx shooterMotor2;

    public static double shooterPower = 0.0;

    // Set this to the encoder ticks per revolution of your motor
    public static double tpr = 28.0;

    private double shooterRPM = 0.0;
    private double highestRPM = 0.0;
    private double lowestRPM = Double.MAX_VALUE;

    private boolean lastX = false;

    @Override
    public void init() {

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());}

    @Override
    public void loop() {
        shooterMotor.setPower(shooterPower);
        shooterMotor2.setPower(-shooterPower);

        double ticksPerSecond = Math.abs(shooterMotor.getVelocity());

        shooterRPM = ticksPerSecond * 60.0 / tpr;

        if (shooterRPM > highestRPM) {
            highestRPM = shooterRPM;}

        if (shooterRPM < lowestRPM) {
            lowestRPM = shooterRPM;}

        if (gamepad1.x && !lastX) {highestRPM = shooterRPM;lowestRPM = shooterRPM;}

        lastX = gamepad1.x;

        telemetry.addData("Shooter Power", "%.2f", shooterPower);
        telemetry.addData("Shooter RPM", "%.0f", shooterRPM);
        telemetry.addData("Highest RPM", "%.0f", highestRPM);
        telemetry.addData("Lowest RPM", "%.0f", lowestRPM);
        telemetry.addData("RPM Drop", "%.0f", highestRPM - lowestRPM
        );

        telemetry.update();
    }

    @Override
    public void stop() {
        shooterMotor.setPower(0);
        shooterMotor2.setPower(0);
    }
}