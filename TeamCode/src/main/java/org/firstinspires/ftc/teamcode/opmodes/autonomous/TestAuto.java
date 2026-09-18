package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static com.pedropathing.ivy.Scheduler.schedule;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redFortyEightStartingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.testAutoStart;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.routines.RedFortyEightRoutine;
import org.firstinspires.ftc.teamcode.routines.TestAutoRoutine;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;

@Autonomous
public class TestAuto extends OpMode {
    //  private Intake intake;
    private Follower follower;
    //  private DcMotorEx intakeMotor;


    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(testAutoStart);
        follower.update();
        // intake = new Intake(hardwareMap); // Sets my intake as the intake from the hardwareMap
        // intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
    }

    @Override
    public void start() {

        schedule(TestAutoRoutine.mainAuto(follower));
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here
        // telemetry.addData("Intaking:", intakeMotor.isMotorEnabled() );
        telemetry.update();
    }

    @Override
    public void stop() {
        FollowerStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
}

