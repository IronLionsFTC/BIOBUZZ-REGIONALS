package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class Localise {

    private static Follower follower;

    public static void init(Follower follower) {
        Localise.follower = follower;
    }

    public static void setPoint(Pose pose) {
        follower.setPose(pose);
    }
}
