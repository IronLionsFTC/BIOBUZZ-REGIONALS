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

import org.firstinspires.ftc.teamcode.math.AimingTargetMath;
import org.firstinspires.ftc.teamcode.math.ShooterLookupTables;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.PollenHood;

@Autonomous
public class Auto extends OpMode {

    // DEFINING //////////////////////////////////////////////////////////////////////////////////
    private Follower follower;
    private PollenHood pollenHood;
    private AimingTargetMath aimingTargetMath;
    private ShooterLookupTables shooterLookupTables;
    private static final PoseFactory poseFactory = PoseFactory.degrees();


    // POSES ///////////////////////////////////////////////////////////////////////////////////
    private static final Pose redAutoStart = poseFactory.of(62, 8.3, 90);
    private static final Pose southShootingPosition = poseFactory.of(47, 23.5, 245);
    private static final Pose redBallSpike = poseFactory.of(10.25, 9.8, 180);
    private static final Pose controlPoint = poseFactory.of(43.6, 11.8, 0);
    private static final Pose northShootingPosition = poseFactory.of(47, 117, 115);


    // PATHS ////////////////////////////////////////////////////////////////////////////////////
    private static Path shootPreloads() {
        return line(redAutoStart, southShootingPosition)
                .linear(redAutoStart, southShootingPosition);}
    private static Path goToBallSpike(){
        return curve(southShootingPosition, controlPoint, redBallSpike)
                .linear(southShootingPosition, redBallSpike);}
    private static Path goToNorthShooting(){
        return line(redBallSpike, northShootingPosition)
                .linear(redBallSpike, northShootingPosition);}


    // IVY COMMAND ///////////////////////////////////////////////////////////////////////////////
    private static Command mainAuto(Follower follower) {
        return sequential(
                follow(follower, shootPreloads()),
                follow(follower,goToBallSpike()),
                follow(follower, goToNorthShooting())
        );
    }

    // OPMODE CODE ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        aimingTargetMath = new AimingTargetMath(follower);
        pollenHood = new PollenHood(hardwareMap);
        shooterLookupTables = new ShooterLookupTables();

        follower.setPose(redAutoStart);
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

        // Run hood code
        double shootingDistance = aimingTargetMath.getDistanceToTarget();
        double pollenTargetHood = shooterLookupTables.getPollenHood(shootingDistance);
        pollenHood.setAngle(pollenTargetHood);

        // Add data
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Busy", follower.isBusy());
        telemetry.addData("Completion", follower.completion());
        telemetry.addData("Hood Angle", pollenTargetHood);

        telemetry.update();
    }
}