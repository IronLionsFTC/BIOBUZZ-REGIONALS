/*
package org.firstinspires.ftc.teamcode.opmodes.autonomous; // make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.ivy.Scheduler.*;
import static com.pedropathing.ivy.pedro.PedroCommands.*;
import static com.pedropathing.ivy.groups.Groups.*;

import org.firstinspires.ftc.teamcode.SubSystems.Intake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


/// Required Imports and starting...
@Autonomous(name = "Square Auto", group = "Tests")
public class IvyAutonomous extends OpMode {

    /// Starting Follower
    private Follower follower;

    private Intake intake;
    /// Here we init the poses that we want. This is where the robot will go to and then back to
    /// you can do whatever you want for these, use visualiser to find what is wanted
    private final Pose startPose = new Pose(60, 60, Math.toRadians(90)); // Start Pose of our robot. This is against the goal facing AWAY
    private final Pose topRight = new Pose(70, 118, Math.toRadians(180)); // Scoring Pose of our robot.
    private final Pose topLeft = new Pose(23, 118, Math.toRadians(180)); // Highest (First Set) of Artifacts from the Spike Mark.
    private final Pose backLeft = new Pose(23, 70, Math.toRadians(180)); // Middle (Second Set) of Artifacts from the Spike Mark.
    private final Pose backRight = new Pose (70, 70, Math.toRadians(180));

    //Here we define the path chains, the different Poses/paths that the robot will go to.
    private PathChain firstCorner, secondCorner, thirdCorner, fourthCorner, returnToStart;
    public void buildPaths() {
        firstCorner = follower.pathBuilder()
                .addPath(new BezierLine(startPose, backRight))
                .setLinearHeadingInterpolation(startPose.getHeading(), topRight.getHeading())
                .build();



        //This is the way to build the paths to go from one point to another point.
        // This one uses a bezier line to go left (to complete the square)
        secondCorner = follower.pathBuilder()
                .addPath(new BezierLine(topRight, topLeft))
                .setLinearHeadingInterpolation(topRight.getHeading(), topLeft.getHeading())
                .build();

        */
/* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. *//*

        thirdCorner = follower.pathBuilder()
                .addPath(new BezierLine(topLeft, backLeft))
                .setLinearHeadingInterpolation(topLeft.getHeading(), backLeft.getHeading())
                .build();

        */
/* This is our grabPickup2 PathChain. We are using a single path with a BezierCurve (curved line). *//*

        fourthCorner = follower.pathBuilder()
                .addPath(new BezierCurve(backLeft, new Pose(19, 95), backRight))
                .setLinearHeadingInterpolation(backLeft.getHeading(), backRight.getHeading())
                .build();

        */
/* This is our scorePickup2 PathChain. We are using a single path with a BezierCurve (curved line). *//*

        returnToStart = follower.pathBuilder()
                .addPath(new BezierLine(backRight, startPose))
                .setLinearHeadingInterpolation(backRight.getHeading(), startPose.getHeading())
                .build();
    }
    public Command autoRoutine() {
        return sequential(
                */
/* Goes in a square*//*

                follow(follower, firstCorner, true),
                follow(follower, secondCorner, true),
                follow(follower, thirdCorner, true),
                follow(follower, fourthCorner, true),
                follow(follower, returnToStart, true)

        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

    }

    @Override
    public void start(){
        schedule(autoRoutine());

    }

    @Override
    public void loop(){
        follower.update();
        Scheduler.execute();

        // Feedback to Driver Hub for debugging
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }


}

*/
