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


    public static final Pose redLocalisationPoint =
            poseFactory.of(7.7, 9.1, 270);
    public static final Pose blueLocalisationPoint =
            poseFactory.of(134.2, 132.5, 90);





}
