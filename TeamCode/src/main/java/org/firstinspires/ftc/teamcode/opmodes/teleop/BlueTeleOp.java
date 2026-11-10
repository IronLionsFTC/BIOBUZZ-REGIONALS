package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redLocalisationPoint;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.GoToPoint;
import org.firstinspires.ftc.teamcode.commands.Localise;
import org.firstinspires.ftc.teamcode.commands.Shoot;
import org.firstinspires.ftc.teamcode.math.AimingTargetMath;
import org.firstinspires.ftc.teamcode.math.ShooterLookupTables;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Aiming;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;
import org.firstinspires.ftc.teamcode.subsystems.IntakeTransfer;
import org.firstinspires.ftc.teamcode.subsystems.NectarHood;
import org.firstinspires.ftc.teamcode.subsystems.PollenHood;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterBlocker;


/// When u can there needs to be a variable of BLUE or RED... aiming math needs to identify that before running
/// so it can always aim at the right goal.
@TeleOp
public class BlueTeleOp extends OpMode {


    private Follower follower;
    private Aiming aiming;
    private PollenHood pollenHood;
    private NectarHood nectarHood;
    private Shooter shooter;
    private Shoot shoot;
    private IntakeTransfer intakeTransfer;
    private ShooterBlocker shooterBlocker;
    private ShooterLookupTables shooterLookupTables;
    private AimingTargetMath aimingTargetMath;


    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Localise.init(follower);
        GoToPoint.init(follower);

        aiming = new Aiming(follower);

        aimingTargetMath = new AimingTargetMath(follower);

        shooter = new Shooter(hardwareMap);
        pollenHood = new PollenHood(hardwareMap);
        nectarHood = new NectarHood(hardwareMap);

        intakeTransfer = new IntakeTransfer(hardwareMap);
        shooterBlocker = new ShooterBlocker(hardwareMap);
        shoot = new Shoot(intakeTransfer, shooterBlocker);

        shooterLookupTables = new ShooterLookupTables();



    }


    @Override
    public void start() {
        follower.setPose(FollowerStorage.autonomousEndPose);
        follower.update();
    }


    @Override
    public void loop() {

        if (gamepad1.circleWasPressed()) {
            GoToPoint.goToRedShootingPosition();
        }

        if (gamepad1.dpad_left) {
            Localise.setPoint(redLocalisationPoint);
        }

        if (gamepad1.squareWasPressed()) {
            aiming.toggle();
        }

        if (gamepad1.right_trigger_pressed) {
            shoot.shoot();
        }
        else
            shoot.noShoot();

        if (gamepad1.left_trigger_pressed) {
            intakeTransfer.run();
        }
        else intakeTransfer.stop();


        // ==================
        //   Actual Driving
        // ==================

        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn;

        if (aiming.isActive()) {
            turn = aiming.getTurnPower();
        } else {
            turn = -gamepad1.right_stick_x;
        }

        if (!follower.isBusy()) {
            follower.manual(
                    forward,
                    strafe,
                    turn
            );
        }

        follower.update();
        Pose robotPose = follower.pose();

        double shootingDistance = aimingTargetMath.getDistanceToTarget();

        double targetRPM = shooterLookupTables.getShooterRPM(shootingDistance);

        double nectarTargetHood = shooterLookupTables.getNectarHood(shootingDistance);
        double pollenTargetHood = shooterLookupTables.getPollenHood(shootingDistance);

        shooter.setTargetRPM(targetRPM);
        shooter.update();

        pollenHood.setAngle(pollenTargetHood);
        nectarHood.setAngle(nectarTargetHood);



        // ===================
        //  Telemetry Values
        // ===================

        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Aim Error", Math.toDegrees(aiming.getHeadingError()));
        telemetry.addData("Robot Target X", aimingTargetMath.getTargetX());
        telemetry.addData("Robot Target Y", aimingTargetMath.getTargetY());
        telemetry.addData("Robot Direct Distance from Target", aimingTargetMath.getDistanceToTarget());
        telemetry.addData("Target RPM of Shooters", targetRPM);

        telemetry.addData("Measured Shooter RPM", "%.0f", shooter.getCurrentRPM());
        telemetry.addData("Shooter RPM Error", "%+.0f", shooter.getRPMError());
        telemetry.addData(  "Shooter Motor Power", "%.3f", shooter.getOutputPower());
        telemetry.addData("Shooter Ready", shooter.isReady());

        telemetry.addData("Pollen Target Hood", pollenTargetHood);
        telemetry.addData("Nectar Target Hood", nectarTargetHood);
        telemetry.update();

    }

    @Override
    public void stop() {

        if (intakeTransfer != null) {
            intakeTransfer.stop();
        }

        if (shooter != null) {
            shooter.stop();
        }

        if (shooterBlocker != null) {
            shooterBlocker.close();
        }
    }
}