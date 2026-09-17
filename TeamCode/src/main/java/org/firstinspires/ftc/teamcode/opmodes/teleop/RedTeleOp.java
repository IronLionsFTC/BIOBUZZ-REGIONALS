package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.commands.GoToPoint;
import org.firstinspires.ftc.teamcode.commands.Localise;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.PositionDependentAiming;
import org.firstinspires.ftc.teamcode.subsystems.TeleOpDrive;

import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redLocalisationPoint;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.AIM_DEADBAND_DEGREES;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class RedTeleOp extends OpMode {

    private Follower follower;
    private PositionDependentAiming positionDependentAiming;
    private TeleOpDrive teleOpDrive;


    @Override
    public void init() {

        follower = Constants.create(hardwareMap);

        Localise.init(follower);
        GoToPoint.init(follower);

        positionDependentAiming = new PositionDependentAiming(follower);

        teleOpDrive = new TeleOpDrive(
                follower,
                positionDependentAiming
        );
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
            positionDependentAiming.toggle();
        }


        // ==================
        //   Actual Driving
        // ==================

        teleOpDrive.drive(
                gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );

        teleOpDrive.update();

        Pose robotPose = teleOpDrive.getPose();


        // ===================
        //  Telemetry Values
        // ===================

        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Robot Target Heading", Math.toDegrees(positionDependentAiming.getHeadingError()));
        telemetry.addData("Turn Power", positionDependentAiming.getTurnPower());
        telemetry.addData("Auto turn is active", positionDependentAiming.isActive());
        telemetry.addData("Aim Error", Math.toDegrees(positionDependentAiming.getHeadingError()));
        telemetry.addData("Deadband", AIM_DEADBAND_DEGREES);
    }
}