package org.firstinspires.ftc.teamcode.commands;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.INTAKE_DEFAULT_POWER;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_BLOCKER_CLOSED_POSITION;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_BLOCKER_OPEN_POSITION;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.TIME_TO_SHOOT;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.TRANSFER_DEFAULT_POWER;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Shoot {

    private final DcMotorEx intake;
    private final DcMotorEx transfer;
    private final Servo blocker1;
    private final Servo blocker2;

    private final ElapsedTime timer = new ElapsedTime();

    public Shoot(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");
        blocker1 = hardwareMap.get(Servo.class, "shooterBlocker1");
        blocker2 = hardwareMap.get(Servo.class, "shooterBlocker2");
    }

    public Command shoot() {
        return Command.build()
                .setStart(() -> {
                    timer.reset();

                    blocker1.setPosition(SHOOTER_BLOCKER_OPEN_POSITION);
                    blocker2.setPosition(SHOOTER_BLOCKER_OPEN_POSITION);

                    intake.setPower(INTAKE_DEFAULT_POWER);
                    transfer.setPower(TRANSFER_DEFAULT_POWER);
                })
                .setDone(() -> timer.seconds() >= TIME_TO_SHOOT)
                .setEnd(endCondition -> {
                    intake.setPower(0);
                    transfer.setPower(0);

                    blocker1.setPosition(SHOOTER_BLOCKER_CLOSED_POSITION);
                    blocker2.setPosition(SHOOTER_BLOCKER_CLOSED_POSITION);
                })
                .requiring(intake);
    }
}