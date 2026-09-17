package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import static com.pedropathing.api.Paths.line;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redNorthShootingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redSouthShootingPose;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_MIDDLE_POINT_Y;

public class GoToPoint {

    private static Follower follower;

    public static void init(Follower follower) {
        GoToPoint.follower = follower;
    }

    public static void goTo(Pose pose) {
        follower.follow(
                line(follower.pose(), pose)
                        .linear(follower.pose(), pose)
        );
    }

    public static void goToRedShootingPosition() {

        Pose robotPose = follower.pose();

        if (robotPose.y() >= RED_MIDDLE_POINT_Y) {
            goTo(redNorthShootingPose);
        } else {
            goTo(redSouthShootingPose);
        }
    }
}