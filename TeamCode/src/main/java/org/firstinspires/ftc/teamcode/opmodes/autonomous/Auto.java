package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
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
import static com.pedropathing.ivy.commands.Commands.waitMs;
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
    private static final Pose redAutoStart = poseFactory.of(62, 8.3, 270);
    private static final Pose redSouthShootingPosition = poseFactory.of(47, 23.5, 245);
    private static final Pose redBallSpike = poseFactory.of(10.25, 10.25, 180);
    private static final Pose redControlPoint = poseFactory.of(43.6, 12, 0);
    private static final Pose redNorthShootingPosition = poseFactory.of(47, 117, 115);
    private static final Pose redControlPoint2 = poseFactory.of(12.5, 107,0);
    private static final Pose redNorthFlower = poseFactory.of(45.5, 129, 115);
    private static final Pose redPark = poseFactory.of(20.1, 117.2, 90);


    // PATHS ////////////////////////////////////////////////////////////////////////////////////
    private static Path shootPreloads() {
        return line(redAutoStart, redSouthShootingPosition)
                .linear(redAutoStart, redSouthShootingPosition);}
    private static Path goToBallSpike(){
        return curve(redSouthShootingPosition, redControlPoint, redBallSpike)
                .linear(redSouthShootingPosition, redBallSpike);}
    private static Path goToNorthShooting(){
        return curve(redBallSpike, redControlPoint2, redNorthShootingPosition)
                .linear(redBallSpike, redNorthShootingPosition);}
    private static Path intakeNorthFlower(){
        return line(redNorthShootingPosition, redNorthFlower)
                .linear(redNorthShootingPosition, redNorthFlower);}
    private static Path shootFlowerBalls(){
        return line(redNorthFlower, redNorthShootingPosition)
                .linear(redNorthFlower, redNorthShootingPosition);}
    private static Path goToPark(){
        return line(redNorthShootingPosition, redPark)
                .linear(redNorthShootingPosition, redPark);}


    // IVY COMMAND ///////////////////////////////////////////////////////////////////////////////
    private static Command mainAuto(Follower follower) {
        return sequential(
                follow(follower, shootPreloads()),
                waitMs(1000),
                follow(follower,goToBallSpike()),
                follow(follower, goToNorthShooting()),
                waitMs(1000),
                follow(follower, intakeNorthFlower()),
                waitMs(500),
                follow(follower, shootFlowerBalls()),
                waitMs(1000),
                follow(follower, goToPark())
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
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


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