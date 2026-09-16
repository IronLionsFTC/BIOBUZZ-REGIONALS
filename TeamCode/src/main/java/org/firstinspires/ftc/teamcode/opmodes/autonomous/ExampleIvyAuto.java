package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.intakeNorthFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.intakeSecondFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.intakeSpike;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.shootNorthFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.shootPreload;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.shootSecondFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.shootSpikeBalls;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.exampleIvyAutoStart;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.northFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.northShootingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redBallSpike;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.southFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.southShootingPose;
import static java.util.concurrent.locks.LockSupport.park;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.routines.ExampleIvyRoutine;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@Autonomous
public class ExampleIvyAuto extends OpMode {
  //  private Intake intake;
    private Follower follower;
  //  private DcMotorEx intakeMotor;


    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(exampleIvyAutoStart);
        follower.update();
        // intake = new Intake(hardwareMap); // Sets my intake as the intake from the hardwareMap
       // intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
    }

    @Override
    public void start() {

        schedule(ExampleIvyRoutine.mainAuto(follower));
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here
        // telemetry.addData("Intaking:", intakeMotor.isMotorEnabled() );
        telemetry.update();
    }

    @Override
    public void stop() {
        FollowerStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
}

