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


import static java.util.concurrent.locks.LockSupport.park;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@Autonomous
public class ExampleIvyAuto extends OpMode {
    private Intake intake;
    private Follower follower;
    private DcMotorEx intakeMotor;
    private final PoseFactory poseFactory = PoseFactory.degrees();




    // ================= //
    //       POSES
    // ================= //
    private final Pose startPose = poseFactory.of(8.282926829268291, 8.327642276422754, 90);
    private final Pose southShootingPose = poseFactory.of(46.2, 23.8, 260);
    private final Pose redBallSpike = poseFactory.of(9.1, 8.7, 180);
    private final Pose southFlower = poseFactory.of(11, 45, 190);
    private final Pose northShootingPose = poseFactory.of(48,118,100);
    private final Pose northFlower = poseFactory.of(45.8, 131.2, 100);
    private final Pose park = poseFactory.of(9.7, 119, 0);


    // ==================== //
    // PATHS LISTED BELOW
    // ==================== //


    private Path shootPreload() {
        return line(startPose, southShootingPose).linear(startPose, southShootingPose);
    }

    private Path intakeSpike(){
        return line(southShootingPose, redBallSpike).linear(southShootingPose, redBallSpike);
    }

    private Path shootSpikeBalls(){
        return line(redBallSpike, northShootingPose).linear(redBallSpike, northShootingPose);
    }

    private Path intakeNorthFlower(){
        return line(northShootingPose, northFlower).linear(southShootingPose, northFlower);
    }

    private Path shootNorthFlower(){
        return line(northFlower, northShootingPose).linear(northFlower, northShootingPose);
    }

    private Path intakeSecondFlower(){
        return line(northShootingPose, southFlower).linear(northShootingPose, southFlower);
    }

    private Path shootSecondFlower(){
        return line(southFlower, southShootingPose).linear(southFlower, southShootingPose);
    }




    // ============== //
    // IVY COMMANDS
    // ============= //

    private Command autoRoutine() {
        return sequential(
                follow(follower, shootPreload()),
                // Add mechanism commands here, below is an intake example from Intake.java

                deadline(
                        follow(follower, intakeSpike()),
                        intake.intake()
                ), //deadline states that the second one finishes when the first finishes. (thus we dont get caught in a loop of infinite intake)
                follow(follower, shootSpikeBalls()),

                deadline(
                        follow(follower, intakeNorthFlower()),
                        intake.intake()
                ),
                follow(follower, shootNorthFlower()),

                deadline(
                        follow(follower, intakeSecondFlower()),
                        intake.intake()
                ),

                follow(follower, shootSecondFlower())

        );
    }



    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
        intake = new Intake(hardwareMap); // Sets my intake as the intake from the hardwareMap
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.addData("Intaking:", intakeMotor.isMotorEnabled() );
        telemetry.update();
    }

    // in your autonomous


    @Override
    public void stop() {
        FollowerStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
}
