package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.MAX_HOOD_ANGLE;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.MAX_SERVO_POSITION;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.MIN_HOOD_ANGLE;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.MIN_SERVO_POSITION;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


public class PollenHood {

    private static final String SERVO_NAME = "pollenHood";


    private final Servo servo;

    private double targetAngle = 0.0;


    public PollenHood(HardwareMap hardwareMap) {

        servo = hardwareMap.get(
                Servo.class,
                SERVO_NAME
        );
    }


    public void setAngle(double angle) {

        targetAngle = Range.clip(
                angle,
                MIN_HOOD_ANGLE,
                MAX_HOOD_ANGLE
        );

        double servoPosition =
                MIN_SERVO_POSITION
                        + (targetAngle - MIN_HOOD_ANGLE)
                        / (MAX_HOOD_ANGLE - MIN_HOOD_ANGLE)
                        * (MAX_SERVO_POSITION - MIN_SERVO_POSITION);

        servo.setPosition(servoPosition);
    }


    public double getTargetAngle() {

        return targetAngle;
    }


    public double getPosition() {

        return servo.getPosition();
    }
}