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
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.paths.interpolator.Interpolator.linear;

import org.firstinspires.ftc.teamcode.math.AimingTargetMath;
import org.firstinspires.ftc.teamcode.math.ShooterLookupTables;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Flower;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.PollenHood;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterBlocker;

@Autonomous
public class ZaraAuto extends OpMode {

    // DEFINING //////////////////////////////////////////////////////////////////////////////////
    private Follower follower;
    private PollenHood pollenHood;
    private AimingTargetMath aimingTargetMath;
    private ShooterLookupTables shooterLookupTables;
    private Shooter shooter;
    private Intake intake;
    private Transfer transfer;
    private Flower flower;
    ElapsedTime shootTimer = new ElapsedTime();
    ElapsedTime flowerTimer = new ElapsedTime();
    private ShooterBlocker shooterBlocker;
    private static final PoseFactory poseFactory = PoseFactory.degrees();


    // POSES ///////////////////////////////////////////////////////////////////////////////////
    private static final Pose autoStart = poseFactory.of(57.68880491155383, 131.82903621754696, 90);
    private static final Pose controlPoint = poseFactory.of(25.57, 47.16, 270);
    private static final Pose ballSpike = poseFactory.of(7.38, 8.4, 270);
    private static final Pose ballSpikeAgain = poseFactory.of(7.38, 10, 270);
    private static final Pose underGoal = poseFactory.of(55.47, 38.96, 90);
    private static final Pose shootSpikeBalls = poseFactory.of(57.73, 122.12, 90);
    private static final Pose goBackForFlower = poseFactory.of(53.07, 115, 90);
    private static final Pose flowerIntake = poseFactory.of(42.6, 125.3, 90);
    private static final Pose controlPoint2 = poseFactory.of(19.75, 96.3, 180 );
    private static final Pose goToFlower2 = poseFactory.of(27.46, 62.36, 180);
    private static final Pose intakeFlower2 = poseFactory.of(17, 46.23, 180);
    private static final Pose shootLastMinuteBalls = poseFactory.of(43.49, 122.29, 113);
    private static final Pose park = poseFactory.of(14.15, 123.59, 0);



    // PATHS //////////////////////////////////////////////////////////////////////////////////

    private static Path goForwardsAfterPreloads() {
        return line(autoStart, shootSpikeBalls)
                .linear(autoStart, shootSpikeBalls);
    }
    private static Path goBackToFlower() {
        return line(shootSpikeBalls, goBackForFlower)
                .linear(shootSpikeBalls, goBackForFlower)
                .with(
                        Constants.foresightConfig.maxPathSpeed.at(0.8)
                );
    }

    private static Path intakeFlower() {
        return line(goBackForFlower, flowerIntake)
                .linear(goBackForFlower, flowerIntake)
                .with(
                        Constants.foresightConfig.maxPathSpeed.at(0.7)
                );
    }

    private static Path shootFlowerBalls1() {
        return line(flowerIntake, shootSpikeBalls)
                .linear(flowerIntake, shootSpikeBalls);
    }


    private static Path parkForReal() {
        return line(shootSpikeBalls, park)
                .linear(shootSpikeBalls, park);
    }

    Command flowerDown = Command.build()
            .setStart(() -> {
                flowerTimer.reset();
            })
            .setExecute(() -> {
                flower.down();
            })
            .setDone(() -> flowerTimer.seconds() >= 1.5);

    Command flowerUp = Command.build()
            .setStart(() -> {
                flowerTimer.reset();
            })
            .setExecute(() -> {
                flower.up();
            })
            .setDone(() -> flowerTimer.seconds() >= 1)
            .setEnd(endCondition -> {
                flower.up();
            });



    Command intakeBalls = Command.build()
            .setExecute(() -> {
                intake.run();
                transfer.run();
            })
            .setEnd(endCondition -> {
                intake.stop();
                transfer.stop();
            });

    Command flowerIntakeCommand = Command.build()
            .setStart(() -> {
                flowerTimer.reset();
            })
            .setExecute(() -> {
                intake.run();
                transfer.run();
            })
            .setDone(() -> flowerTimer.seconds() >= 2.0)
            .setEnd(endCondition -> {
                intake.stop();
                transfer.stop();
            })
            .requiring(intake, transfer);



    Command shootBalls = Command.build()
            .setStart(() -> {
                shootTimer.reset();
                shooterBlocker.open();
            })
            .setExecute(() -> {
                intake.run();
                transfer.run();
                shooterBlocker.open();
            })
            .setDone(() -> shootTimer.seconds() >= 2.0)
            .setEnd(endCondition -> {
                intake.stop();
                transfer.stop();
                shooterBlocker.close();
            })
            .requiring(intake, transfer, shooterBlocker);



    private Command mainAuto(Follower follower) {
        return sequential(
                waitMs(9000),
                shootBalls,
                follow(follower, goForwardsAfterPreloads()),
                parallel(
                        follow(follower,goBackToFlower()),
                        flowerDown),
                follow(follower, intakeFlower()),
                flowerIntakeCommand,
                flowerIntakeCommand,
                deadline(
                        follow(follower, shootFlowerBalls1()),
                        flowerUp
                ),
                waitMs(1000),
                shootBalls,
                follow(follower, parkForReal())
        );
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
        flower = new Flower(hardwareMap);
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        shooterBlocker = new ShooterBlocker(hardwareMap);


        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        shooterBlocker.close();


        follower.setPose(autoStart);
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

        double targetRPM = shooterLookupTables.getShooterRPM(shootingDistance);
        shooter.setTargetRPM(targetRPM);
        shooter.update();


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


        telemetry.update();
    }

    @Override
    public void stop() {


        FollowerStorage.autonomousEndPose = follower.pose();

        if (intake != null) {
            intake.stop();
        }

        if (shooter != null) {
            shooter.stop();
        }

        if (shooterBlocker != null) {
            shooterBlocker.close();
        }

    }
}