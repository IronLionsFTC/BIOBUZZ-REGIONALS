package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.commands.GoToPoint;
import org.firstinspires.ftc.teamcode.commands.Localise;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.math.AimingTargetMath;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.Aiming;

import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redLocalisationPoint;




@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class RedTeleOp extends OpMode {


    private Follower follower;
    private Aiming aiming;
    private AimingTargetMath aimingTargetCalculator;



    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Localise.init(follower);
        GoToPoint.init(follower);

        aiming = new Aiming(follower);
    }


    @Override
    public void start() {

        follower.setPose(FollowerStorage.autonomousEndPose);
        follower.update();
    }


    @Override
    public void loop() {

        if (gamepad1.circleWasPressed()) {
            GoToPoint.goToRedShootingPosition();
        }

        if (gamepad1.dpad_left) {
            Localise.setPoint(redLocalisationPoint);
        }

        if (gamepad1.squareWasPressed()) {
            aiming.toggle();
        }


        // ==================
        //   Actual Driving
        // ==================

        double forward = -gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double turn;

        if (aiming.isActive()) {
            turn = aiming.getTurnPower();
        } else {
            turn = -gamepad1.right_stick_x;
        }

        if (!follower.isBusy()) {
            follower.manual(
                    forward,
                    strafe,
                    turn
            );
        }

        follower.update();


        Pose robotPose = follower.pose();


        // ===================
        //  Telemetry Values
        // ===================

        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Aim Error", Math.toDegrees(aiming.getHeadingError()));
        telemetry.addData("Robot Target X", aimingTargetCalculator.getTargetX());
        telemetry.addData("Robot Target Y", aimingTargetCalculator.getTargetY());
        telemetry.addData("Robot Direct Distance from Target", aimingTargetCalculator.getDistanceToTarget());
        telemetry.update();

    }
}