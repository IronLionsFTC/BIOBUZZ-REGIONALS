package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import static com.pedropathing.api.Paths.line;

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
}