package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.INTAKE_DEFAULT_POWER;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.TRANSFER_DEFAULT_POWER;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Transfer {
    private final DcMotorEx transfer;

    public Transfer(HardwareMap hardwareMap) {
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");

    }

    public void run() {
        transfer.setPower(TRANSFER_DEFAULT_POWER);
    }

    public void outtake() {
        transfer.setPower(-1);
    }

    public void stop() {
        transfer.setPower(0);
    }

    public void auto() {
        transfer.setPower(0.4);
    }

    public void setPower(double power) {
        transfer.setPower(power);
    }
}