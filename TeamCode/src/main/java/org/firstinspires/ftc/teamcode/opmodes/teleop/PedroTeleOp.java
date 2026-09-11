/*
package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.bylazar.telemetry.PanelsTelemetry;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareType;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.SubSystems.Intake;


import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.function.Supplier;

@TeleOp
public class PedroTeleOp extends OpMode {
    /// Creates a follower
    private Follower follower;


    /// Sets the starting point for the Pose
    private static Pose startingPose;
    private boolean automatedDrive;
    private boolean autoIntake;
    /// A way to check if the robot is automatically going to a spot
    private Supplier<PathChain> pathChain;
    ///  A unique way to do the pathchain on a teleop


    @Override
    public void init(){
        /// Sets up the follower

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();


        /// From what I understand, this will put a unique point on the field that pressing a button will go to...
        /// This can be extended from Ivy control framework to schedule an actual task (for example, go to this point)
        /// And then raise the arm, dump the bucket (probably one or two commands together), then assume control again
        pathChain = () -> follower.pathBuilder() //Lazy Curve Generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(60, 60))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(90), 1))
                .build();

    }


    @Override
    public void start(){
        follower.startTeleOpDrive();
    }




    @Override
    public void loop(){
        follower.update();
        if (!automatedDrive) {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,

                    true // Robot Centric (not field centric, it doesnt  change while the robot moves)

            );
        }

        //Automated PathFollowing if you want it to do a specific thing (like go to a point for scoring)
        if (gamepad1.aWasPressed()) {
            follower.followPath(pathChain.get());
            automatedDrive = true;
        }

        //Stop automated following if the follower is done
        if (automatedDrive && (gamepad1.bWasPressed() || !follower.isBusy())) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }

     //   if (gamepad1.leftTriggerWasPressed() && !autoIntake) {
        //}


    }
}
*/
