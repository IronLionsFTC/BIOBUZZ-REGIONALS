// PRESUMED REDUNDANT FOR NOW ///////////////////////////////////////////////////////////////////
// !!!!!!!!!!!!!!!!!!!!!!!!!! ///////////////////////////////////////////////////////////////////
// !!!!!!!!!!!!!!!!!!!!!!!!!! ///////////////////////////////////////////////////////////////////

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


    // Starting Points of Autos
    public static final Pose redFortyEightStartingPose =
            poseFactory.of(47.3, 8.9, 270);


    // Shooting Spots
    public static final Pose redSouthShootingPose =
            poseFactory.of(41.5, 23.2, 245);
    public static final Pose redNorthShootingPose =
            poseFactory.of(41.5, 117.8, 115);


    // POI's on the Field

    public static final Pose redBallSpike =
            poseFactory.of(8.5, 8.7, 180);

    public static final Pose redSouthFlower =
            poseFactory.of(11, 45, 190);

    public static final Pose redNorthFlower =
            poseFactory.of(43.3, 131.1, 95);


    // Important Points on Field
    public static final Pose redPark =
            poseFactory.of(14.1, 116.5, 0);

    public static final Pose redLocalisationPoint =
            poseFactory.of(8.6, 8.46, 270);

    public static final Pose redWaitForTilt =
            poseFactory.of(57.3, 105.1, 0);

    public static final Pose redCollectBallsInMiddle =
            poseFactory.of(58.5, 114.4, 0);

    public static final Pose redCollectBallsOnWall =
            poseFactory.of(60.6, 131.6, 90);


    // Control Points

    public static final Pose redControlPointOfBallSpike =
            poseFactory.of(34.77, 13.8, 212.5);

    public static final Pose redControlPointOfPark =
            poseFactory.of(48, 103, 45);


    // Test Points




}
