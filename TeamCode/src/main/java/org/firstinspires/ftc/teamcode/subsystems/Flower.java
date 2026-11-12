package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.FLOWER_DOWN;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.FLOWER_UP;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_BLOCKER_CLOSED_POSITION;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.SHOOTER_BLOCKER_OPEN_POSITION;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Flower {

    private final Servo flowerServo;

    public Flower(HardwareMap hardwareMap) {
        flowerServo = hardwareMap.get(Servo.class, "flowerServo");
    }

    public void up() {
        flowerServo.setPosition(FLOWER_UP);
    }

    public void open() {
        flowerServo.setPosition(FLOWER_DOWN);
    }

}