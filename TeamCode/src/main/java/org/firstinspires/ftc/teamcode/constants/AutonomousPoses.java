package org.firstinspires.ftc.teamcode.constants;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

public final class AutonomousPoses {

    private static final PoseFactory poseFactory = PoseFactory.degrees();

    // Prevent creating instances of this class
    private AutonomousPoses() {
    }


    // ================= //
    //       POSES
    // ================= //

    public static final Pose exampleIvyAutoStart =
            poseFactory.of(8.282926829268291, 8.327642276422754, 90);

    public static final Pose southShootingPose =
            poseFactory.of(41.50487804878049, 23.185365853658535, 245);

    public static final Pose redBallSpike =
            poseFactory.of(9.1, 8.7, 180);

    public static final Pose southFlower =
            poseFactory.of(11, 45, 190);

    public static final Pose northShootingPose =
            poseFactory.of(48, 118, 100);

    public static final Pose northFlower =
            poseFactory.of(45.8, 131.2, 100);

    public static final Pose park =
            poseFactory.of(9.7, 119, 0);

    public static final Pose localisationPoint =
            poseFactory.of(8.603252032520325, 8.46016260162602, 270);
    }
