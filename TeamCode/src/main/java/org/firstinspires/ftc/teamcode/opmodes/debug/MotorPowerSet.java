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
    private DcMotorEx intake;
    private DcMotorEx transfer;
    private DcMotorEx shooterMotor2;
    private Servo pollenHood;
    private Servo shooterBlocker1;
    private Servo shooterBlocker2;
    private Servo nectarHood;

    public static double shooterPower = 0.0;
    public static double shooterBlockerPosition = 0.0;
    public static double intakePower = 0.0;
    public static double transferPower = 0.0;
    public static double hoodAngle1 = 0.0;
    public static double hoodAngle2 = 0.0;



    @Override
    public void init() {

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter1");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class, "shooter2");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        shooterBlocker1 = hardwareMap.get(Servo.class, "shooterBlocker1");
        shooterBlocker2 = hardwareMap.get(Servo.class, "shooterBlocker2");
        nectarHood = hardwareMap.get(Servo.class, "pollenHood");
        pollenHood = hardwareMap.get(Servo.class, "nectarHood");

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
        transfer.setPower(transferPower);
        intake.setPower(intakePower);

        nectarHood.setPosition(Range.clip(hoodAngle1, 0.0, NECTAR_MAX_SERVO_POSITION));
        pollenHood.setPosition(Range.clip(hoodAngle2, 0.0, POLLEN_MAX_SERVO_POSITION));

        shooterBlocker1.setPosition(shooterBlockerPosition);
        shooterBlocker2.setPosition(shooterBlockerPosition);

        telemetry.update();
    }

    @Override
    public void stop() {

        shooterMotor.setPower(0.0);
        shooterMotor2.setPower(0.0);
    }
}



