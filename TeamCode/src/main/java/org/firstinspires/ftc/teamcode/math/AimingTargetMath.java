package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.MIDDLE_POINT_Y;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_NORTH_TARGET_X;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_NORTH_TARGET_Y;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_SOUTH_TARGET_X;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_SOUTH_TARGET_Y;



public class AimingTargetMath {

    private final Follower follower;

    private double targetX;
    private double targetY;


    public AimingTargetMath(Follower follower) {

        this.follower = follower;

        updateTarget();
    }


    // Select North or South target

    public void updateTarget() {

        Pose robotPose = follower.pose();

        if (robotPose.y() >= MIDDLE_POINT_Y) {

                targetX = RED_NORTH_TARGET_X;
                targetY = RED_NORTH_TARGET_Y;
        } else {
                targetX = RED_SOUTH_TARGET_X;
                targetY = RED_SOUTH_TARGET_Y;

        }
    }



    // Target coordinates

    public double getTargetX() {
        updateTarget();
        return targetX;
    }


    public double getTargetY() {
        updateTarget();
        return targetY;
    }


    // Distance to target


    public double getDistanceToTarget() {

        updateTarget();

        Pose robotPose = follower.pose();

        double dx = targetX - robotPose.x();
        double dy = targetY - robotPose.y();

        return Math.sqrt(
                (dx * dx) + (dy * dy)
        );
    }


    // Angle to target

    public double getAngleToTarget() {

        updateTarget();

        Pose robotPose = follower.pose();

        double dx = targetX - robotPose.x();
        double dy = targetY - robotPose.y();

        return Math.atan2(dy, dx);
    }


    // X distance to target

    public double getDeltaX() {

        updateTarget();

        Pose robotPose = follower.pose();

        return targetX - robotPose.x();
    }


    // Y distance to target

    public double getDeltaY() {

        updateTarget();

        Pose robotPose = follower.pose();

        return targetY - robotPose.y();
    }
}