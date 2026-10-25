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

import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

@Config
@TeleOp
public class MotorPowerSet extends OpMode {

    private DcMotorEx shooterMotor;
    private DcMotorEx shooterMotor2;
    private Servo hood1;
    private Servo hood2;

    public static double shooterPower = 0.1;
    public static double shooterRPM = 0.0;
    public static double shooterRPM2 = 0.0;
    public static double hoodAngle1 = 0.5;
    public static double hoodAngle2 = 0.3;



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
    public void loop() {

        shooterMotor.setPower(shooterPower);
        shooterMotor2.setPower(-shooterPower);

        hood1.setPosition(Range.clip(hoodAngle1, 0.0, NECTAR_MAX_SERVO_POSITION));
        hood2.setPosition(Range.clip(hoodAngle2, 0.0, POLLEN_MAX_SERVO_POSITION));



        telemetry.addData("Shooter Power Target", "%.2f", shooterPower);


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
    }
}



