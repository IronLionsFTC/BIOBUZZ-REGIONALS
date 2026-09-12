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
    private final Pose firstPose = poseFactory.of(98.245, 58.088, 100);
    private final Pose secondPose = poseFactory.of(72.992, 96.998, 200);
    private final Pose lastPose = poseFactory.of(26.1, 48.7, 46);
    private final Pose controlPoint1 = poseFactory.of(75.00894308943089,13.727642276422763,140);


    // Path methods
    private Path firstPoint() {
        return curve(startPose, controlPoint1, firstPose).linear(startPose, firstPose);
    }

    private Path secondPoint(){
        return line(firstPose, secondPose).linear(firstPose, secondPose);
    }

    private Path thirdPoint(){
        return line(secondPose, lastPose).linear(secondPose, lastPose);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, firstPoint()),
                // Add mechanism commands here.
                follow(follower, secondPoint()),
                follow(follower, thirdPoint())
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
