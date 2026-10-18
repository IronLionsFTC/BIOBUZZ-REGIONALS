package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ShooterBlocker {

    private final Servo shooterBlocker1;
    private final Servo shooterBlocker2;

    public ShooterBlocker(HardwareMap hardwareMap) {
        shooterBlocker1 = hardwareMap.get(Servo.class, "shooterBlocker1");
        shooterBlocker2 = hardwareMap.get(Servo.class, "shooterBlocker2");
    }

    public void close() {
        shooterBlocker1.setPosition(0.0);
        shooterBlocker2.setPosition(0.0);
    }

    public void open() {
        shooterBlocker1.setPosition(0.8);
        shooterBlocker2.setPosition(0.8);
    }

}