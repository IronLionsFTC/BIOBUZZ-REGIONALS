
package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class BasicTeleop extends OpMode {
    public DcMotor bl;
    public DcMotor br;
    public DcMotor fr;
    public DcMotor fl;

    public void init(){
        bl = hardwareMap.get(DcMotor.class, "backLeft");
        br = hardwareMap.get(DcMotor.class, "backRight");
        fr = hardwareMap.get(DcMotor.class, "frontRight");
        fl = hardwareMap.get(DcMotor.class,"frontLeft");
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    @Override
    public void loop(){
    double x = gamepad1.left_stick_x;
    double y = gamepad1.left_stick_y;
    double z = gamepad1.right_stick_x;

    bl.setPower(y+x-z);
    br.setPower(y-x+z);
    fr.setPower(y+x+z);
    fl.setPower(-y+x+z);












    }

}

