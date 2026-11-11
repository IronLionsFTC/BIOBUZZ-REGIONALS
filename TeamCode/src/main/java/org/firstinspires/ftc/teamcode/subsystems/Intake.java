package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.INTAKE_DEFAULT_POWER;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.TRANSFER_DEFAULT_POWER;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private final DcMotorEx intake;

    public Intake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");

    }

    public void run() {
        intake.setPower(-INTAKE_DEFAULT_POWER);
    }

    public void outtake() {
        intake.setPower(INTAKE_DEFAULT_POWER);
    }

    public void stop() {
        intake.setPower(0);
    }

    public void setPower(double power) {
        intake.setPower(power);
    }
}