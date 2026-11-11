/// NO COCURRENT THINGS IN HERE RN

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
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.math.AimingTargetMath;
import org.firstinspires.ftc.teamcode.math.ShooterLookupTables;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.HiveVision;
import org.firstinspires.ftc.teamcode.subsystems.DetectHiveSide;
import org.firstinspires.ftc.teamcode.subsystems.PollenHood;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterBlocker;

@Autonomous
public class Prefire extends OpMode {

    // DEFINING //////////////////////////////////////////////////////////////////////////////////
    private Follower follower;
    private PollenHood pollenHood;
    private AimingTargetMath aimingTargetMath;
    private ShooterLookupTables shooterLookupTables;

    private Shooter shooter;
    private Transfer intake;
    private ShooterBlocker shooterBlocker;
    private Shoot shoot;
    private boolean shooterEnabled = true;
    private boolean shootingActive = false;

    // Hive vision system
    private HiveVision hiveVision;
    private DetectHiveSide detectHiveSide;

    private static final PoseFactory poseFactory = PoseFactory.degrees();


    // POSES ///////////////////////////////////////////////////////////////////////////////////
    private static final Pose redAutoStart = poseFactory.of(62, 8.3, 270);
    private static final Pose redSouthShootingPosition = poseFactory.of(47, 23.5, 245);
    private static final Pose redBallSpike = poseFactory.of(10.25, 10.25, 180);
    private static final Pose redControlPoint = poseFactory.of(43.6, 12, 0);
    private static final Pose redNorthShootingPosition = poseFactory.of(47, 117, 115);
    private static final Pose redControlPoint2 = poseFactory.of(12.5, 107, 0);
    private static final Pose redNorthFlower = poseFactory.of(45.5, 129, 115);
    private static final Pose redPark = poseFactory.of(20.1, 117.2, 90);


    // PATHS ////////////////////////////////////////////////////////////////////////////////////
    private static Path shootPreloads() {
        return line(redAutoStart, redSouthShootingPosition)
                .linear(redAutoStart, redSouthShootingPosition);
    }

    private static Path goToBallSpike() {
        return curve(redSouthShootingPosition, redControlPoint, redBallSpike)
                .linear(redSouthShootingPosition, redBallSpike);
    }

    private static Path goToNorthShooting() {
        return curve(redBallSpike, redControlPoint2, redNorthShootingPosition)
                .linear(redBallSpike, redNorthShootingPosition);
    }

    private static Path intakeNorthFlower() {
        return line(redNorthShootingPosition, redNorthFlower)
                .linear(redNorthShootingPosition, redNorthFlower);
    }

    private static Path shootFlowerBalls() {
        return line(redNorthFlower, redNorthShootingPosition)
                .linear(redNorthFlower, redNorthShootingPosition);
    }

    private static Path goToPark() {
        return line(redNorthShootingPosition, redPark)
                .linear(redNorthShootingPosition, redPark);
    }


    // Start a firing burst and wait until the shooting state machine finishes.
    private Command shootCommand() {
        return sequential(
                waitUntil(() -> shooter.isReady()),
                lazy(() -> {
                    shoot.shoot();
                    shootingActive = true;
                    return waitMs(1000);
                }),
                lazy(() -> {
                    shoot.noShoot();
                    shootingActive = false;
                    return waitMs(0);
                })
        );
    }


    // IVY COMMAND ///////////////////////////////////////////////////////////////////////////////

    private Command mainAuto(Follower follower) {
        return sequential(
                follow(follower, shootPreloads()),
                shootCommand(),
                follow(follower, goToBallSpike()),
                follow(follower, goToNorthShooting()),
                shootCommand(),
                lazy(() -> {
                    intake.run();
                    return follow(follower, intakeNorthFlower());
                }),
                waitMs(500),
                lazy(() -> {
                    intake.stop();
                    return waitMs(0);
                }),
                follow(follower, shootFlowerBalls()),
                shootCommand(),
                follow(follower, goToPark()),
                lazy(() -> {
                    shooterEnabled = false;
                    shoot.noShoot();
                    shootingActive = false;
                    intake.stop();
                    shooter.stop();
                    return waitMs(0);
                })

                // Below is the theoretical
//                detectHiveSide.scanCommand(),
//
//
//                lazy(() -> {
//                    if (detectHiveSide.getResult()
//                            == HiveVision.HiveSide.SOUTHSIDEUP) {
//                        return southSideAuto(follower);
//                    }
//
//                    return northSideAuto(follower);
//                })
        );
    }

    // North route: preserves the original sequence.
    private static Command northSideAuto(Follower follower) {
        return sequential(
        );
    }

    // *TODO: Replace this with the actual South-side paths. Then continue this on*
    private static Command southSideAuto(Follower follower) {
        return waitMs(0);
    }


    // OPMODE CODE /////////////////////////////////////////////////////////////////////////////

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        aimingTargetMath = new AimingTargetMath(follower);
        pollenHood = new PollenHood(hardwareMap);
        shooterLookupTables = new ShooterLookupTables();

        shooter = new Shooter(hardwareMap);
        intake = new Transfer(hardwareMap);
        shooterBlocker = new ShooterBlocker(hardwareMap);

        shoot = new Shoot(
                intake,
                shooterBlocker
        );

        shooterEnabled = true;

        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        // Initialise the webcam once.
     //   hiveVision = new HiveVision(hardwareMap, "Webcam 1");
//        detectHiveSide = new DetectHiveSide(hiveVision, telemetry);

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

        double shootingDistance = aimingTargetMath.getDistanceToTarget();

        if (shooterEnabled) {
            double targetRPM = shooterLookupTables.getShooterRPM(shootingDistance);
            shooter.setTargetRPM(targetRPM);
            shooter.update();
        }

        double pollenTargetHood = shooterLookupTables.getPollenHood(shootingDistance);
        pollenHood.setAngle(pollenTargetHood);

        // Add data
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Busy", follower.isBusy());
        telemetry.addData("Completion", follower.completion());
        telemetry.addData("Hood Angle", pollenTargetHood);

        telemetry.addData("Target RPM", shooter.getTargetRPM());
        telemetry.addData("Measured Shooter RPM", "%.0f", shooter.getCurrentRPM());
        telemetry.addData("Shooter RPM Error", "%+.0f", shooter.getRPMError());
        telemetry.addData("Shooter Motor Power", "%.3f", shooter.getOutputPower());
        telemetry.addData("Shooter Ready", shooter.isReady());
        telemetry.addData("Shooting State Active", shootingActive);

        // Add HIVE detection data
     //   telemetry.addData("Hive Side", detectHiveSide.getResult());
    //    telemetry.addData("Hive Fallback Used", detectHiveSide.wasFallbackUsed());
      //  telemetry.addData("Hive Red Clusters", hiveVision.getLastRedClusterCount());
        //telemetry.addData("Hive Detections", hiveVision.getLastSummary());

        telemetry.update();
    }

    @Override
    public void stop() {

        shooterEnabled = false;

        if (shoot != null) {
            shoot.noShoot();
        }

        shootingActive = false;

        if (intake != null) {
            intake.stop();
        }

        if (shooter != null) {
            shooter.stop();
        }

        if (shooterBlocker != null) {
            shooterBlocker.close();
        }

        if (hiveVision != null) {
            hiveVision.close();
        }
    }
}