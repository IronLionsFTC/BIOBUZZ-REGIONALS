package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private final DcMotorEx motor;

    public Intake(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, "intake");
    }

    public void intake() {
        motor.setPower(1.0);
    }

    public void outtake() {
        motor.setPower(-1.0);
    }

    public void stop() {
        motor.setPower(0);
    }

    public void setPower(double power) {
        motor.setPower(power);
    }
}