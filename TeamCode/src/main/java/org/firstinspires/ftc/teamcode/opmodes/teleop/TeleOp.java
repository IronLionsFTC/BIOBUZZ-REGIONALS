package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.commands.Localise;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.GoalAim;
import org.firstinspires.ftc.teamcode.subsystems.PositionDependentAiming;

import static com.pedropathing.api.Paths.line;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends OpMode {

    private Follower follower;
    private PositionDependentAiming positionDependentAiming;



    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        Localise.init(follower);
        positionDependentAiming = new PositionDependentAiming(follower);
    }


    @Override
    public void start(){
        follower.setPose(FollowerStorage.autonomousEndPose);
        follower.update();
    }


    @Override
    public void loop() {

        // ==================
        //   Go to Position
        // ==================

        if (gamepad1.circleWasPressed()) {
            Pose targetPose = new Pose(0, 0, 0);
            follower.follow(
                    line(follower.pose(), targetPose)
                            .linear(follower.pose(), targetPose)
            );
        }

        // ==================
        //     Relocalise
        // ==================

        if (gamepad1.triangleWasPressed()) {
            Localise.setPoint(10,10,90);
        }


        // ==================
        //    Aim at Goal
        // ==================

        if (gamepad1.squareWasPressed()) {
            positionDependentAiming.toggle();
        }

        // ==================
        //   Actual Driving
        // ==================

        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn;

        if (positionDependentAiming.isActive()) {
            turn = positionDependentAiming.getTurnPower();
        } else {
            turn = -gamepad1.right_stick_x;
        }

        if(!follower.isBusy())
            follower.manual(
                    forward,
                    strafe,
                    turn
            );

        follower.update();



        // ===================
        //  Telemetry Values
        // ===================

        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Robot Target Heading", positionDependentAiming.getHeadingError());





        }
    }

