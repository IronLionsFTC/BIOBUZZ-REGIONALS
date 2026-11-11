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
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.math.AimingTargetMath;
import org.firstinspires.ftc.teamcode.math.ShooterLookupTables;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.HiveVision;
import org.firstinspires.ftc.teamcode.subsystems.DetectHiveSide;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
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
    private Intake intake;
    private Transfer transfer;
    private ShooterBlocker shooterBlocker;
    private static final PoseFactory poseFactory = PoseFactory.degrees();


    // POSES ///////////////////////////////////////////////////////////////////////////////////
    private static final Pose redAutoStart = poseFactory.of(54.2, 9.2, 270);




    Command shoot = Command.build()
            .setStart(() -> {
                shooterBlocker.open();
                    })
            .setExecute(() -> {
                intake.run();
                transfer.run();
                    });

    private Command mainAuto(Follower follower) {
        return sequential(
                waitMs(750),
                deadline(
                waitMs(1000),
                shoot
                )
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
        transfer = new Transfer(hardwareMap);
        intake = new Intake(hardwareMap);
        shooterBlocker = new ShooterBlocker(hardwareMap);


        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        shooterBlocker.close();


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