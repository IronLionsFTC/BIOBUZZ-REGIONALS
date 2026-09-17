package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class TeleOpDrive {

    private final Follower follower;
    private final PositionDependentAiming positionDependentAiming;

    public TeleOpDrive(
            Follower follower,
            PositionDependentAiming positionDependentAiming
    ) {
        this.follower = follower;
        this.positionDependentAiming = positionDependentAiming;
    }

    public void drive(double leftStickY, double leftStickX, double rightStickX) {

        double forward = -leftStickY;
        double strafe = -leftStickX;
        double turn;

        if (positionDependentAiming.isActive()) {
            turn = positionDependentAiming.getTurnPower();
        } else {
            turn = -rightStickX;
        }

        if (!follower.isBusy()) {
            follower.manual(
                    forward,
                    strafe,
                    turn
            );
        }
    }

    public void update() {
        follower.update();
    }

    public Pose getPose() {
        return follower.pose();
    }
}