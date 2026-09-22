package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class TestAuto extends OpMode {

    private Follower follower;

    private static final PoseFactory poseFactory = PoseFactory.degrees();

    // Robot starts at 0 degrees
    private static final Pose testAutoStart =
            poseFactory.of(0, 0, 0);

    private static final Pose testPoint =
            poseFactory.of(40, 0, 90);

    private static Path testAutoPath() {
        return line(testAutoStart, testPoint)
                .linear(testPoint, testAutoStart);
    }

    private static Command mainAuto(Follower follower) {
        return sequential(
                follow(
                        follower,
                        testAutoPath()
                )
        );
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);

        follower.setPose(testAutoStart);
        follower.update();
    }

    @Override
    public void start() {
        schedule(mainAuto(follower));
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData(
                "Heading",
                Math.toDegrees(follower.pose().heading())
        );
        telemetry.addData("Busy", follower.isBusy());
        telemetry.addData("Completion", follower.completion());

        telemetry.update();
    }
}