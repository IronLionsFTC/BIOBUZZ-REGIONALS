package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;


import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;

@Autonomous
public class ExampleIvyAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    // Poses
    private final Pose startPose = poseFactory.of(8.282926829268291, 8.327642276422754, 90);
    private final Pose southShootingPose = poseFactory.of(46.2, 23.8, 260);
    private final Pose redBallSpike = poseFactory.of(9.1, 8.7, 180);
    private final Pose firstFlower = poseFactory.of(11, 45, 190);
    private final Pose northShootingPose = poseFactory.of(48,118,100);
    private final Pose secondFlower = poseFactory.of(45.8, 131.2, 100);
    private final Pose park = poseFactory.of(9.7, 119, 0);


    // Path methods
    private Path shootPreload() {
        return line(startPose, southShootingPose).linear(startPose, southShootingPose);
    }

    private Path intakeSpike(){
        return line(southShootingPose, redBallSpike).linear(southShootingPose, redBallSpike);
    }

    private Path shootSpikeBalls(){
        return line(redBallSpike, southShootingPose).linear(redBallSpike, southShootingPose);
    }

    private Path intakeFirstFlower(){
        return line(southShootingPose, firstFlower).linear(southShootingPose, firstFlower);
    }

    private Path shootFirstFlower(){
        return line(firstFlower, northShootingPose).linear(firstFlower, northShootingPose);
    }

    private Path intakeSecondFlower(){
        return line(northShootingPose, secondFlower).linear(northShootingPose, secondFlower);
    }

    private Path shootSecondFlower(){
        return line(secondFlower, northShootingPose).linear(secondFlower, northShootingPose);
    }

    private Path park(){
        return line(northShootingPose, park).linear(northShootingPose, park);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, shootPreload()),
                // Add mechanism commands here.
                follow(follower, intakeSpike()),
                follow(follower, shootSpikeBalls()),
                follow(follower, intakeFirstFlower()),
                follow(follower, shootFirstFlower()),
                follow(follower, intakeSecondFlower()),
                follow(follower, shootSecondFlower()),
                follow(follower, park())
        );
    }



    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
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
        telemetry.update();
    }

    // in your autonomous


    @Override
    public void stop() {
        FollowerStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
}
