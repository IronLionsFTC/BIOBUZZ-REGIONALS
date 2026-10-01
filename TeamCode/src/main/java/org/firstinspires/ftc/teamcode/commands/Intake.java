package org.firstinspires.ftc.teamcode.commands;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.INTAKE_DEFAULT_POWER;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private final DcMotorEx intake;
    private final DcMotorEx transfer;

    public Intake(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
    }

    public Command intake() {
        return Command.build()
                .setExecute(() -> intake.setPower(INTAKE_DEFAULT_POWER))
                .setDone(() -> false)
                .setEnd(endCondition -> intake.setPower(0))
                .requiring(intake);
    }

    public Command reverse() {
        return Command.build()
                .setExecute(() -> intake.setPower(-1.0))
                .setDone(() -> false)
                .setEnd(endCondition -> intake.setPower(0))
                .requiring(intake);
    }

    public Command stop() {
        return Command.build()
                .setStart(() -> intake.setPower(0))
                .setDone(() -> true);
    }
}

