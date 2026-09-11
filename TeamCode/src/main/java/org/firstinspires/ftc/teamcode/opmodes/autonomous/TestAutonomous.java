/*
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.sql.Time;


/// Uses a state machine
///
/// REFER TO IVY AUTONOMOUS FOR EXAMPLE IVY AUTO
@Autonomous
public class TestAutonomous extends OpMode {
    /// Initialisation Points and Variables
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState{
        /// This is a pathstate, where we list and create the states we are using
        /// There are multiple states you run, drive/movement, shooting or scoring, intake...
        driveToTestTile,
        spinAround

    }

    PathState pathState;

    private final Pose initialPose = new Pose(70, 70, Math.toRadians(90));
    private final Pose finalPose = new Pose(70, 180, Math.toRadians(180));

    private PathChain driveInitial_endFinal;

    public void buildPaths() {
        /// To build a path, you put in coordiantes for a starting pose, and then an ending pose
        driveInitial_endFinal = follower.pathBuilder()
                .addPath(new BezierLine(initialPose, finalPose))
                .setLinearHeadingInterpolation(initialPose.getHeading(), finalPose.getHeading())
                .build();


    }

    public void statePathUpdate(){
        switch (pathState) {
            case driveToTestTile:
                follower.followPath(driveInitial_endFinal, true);
                setPathState(PathState.driveToTestTile); //use below function to also reset timer and queue next path
                break;

            case spinAround:
                /// Here you add logic, for example check the flywheel speed, check the servo pos
                /// Now here you check if the follower is currently following a line
                if (follower.isBusy()) {
                    telemetry.addLine("Finished Path");
                /// Transition to another state and repeat until finished path... but this done them
                /// linear and doesn't allow for multiple at a time
                }
                break;

            default:
                telemetry.addLine("There is no current Path");
        }
    }

    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init(){
        pathState = PathState.driveToTestTile;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        /// Add in any other init mechanisms
        /// Limelight, slides, flywheel, intake

        buildPaths();
        follower.setPose(initialPose);
    }

    public void start(){
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop(){
        follower.update();
        statePathUpdate();

        telemetry.addData("Path State:", pathState.toString());
        telemetry.addData("Path time:", pathTimer.getElapsedTime());

    }
}
*/
