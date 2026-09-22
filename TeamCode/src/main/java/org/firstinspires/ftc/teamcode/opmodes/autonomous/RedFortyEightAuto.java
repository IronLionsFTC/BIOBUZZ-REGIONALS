// PRESUMED REDUNDANT FOR NOW ///////////////////////////////////////////////////////////////////
// !!!!!!!!!!!!!!!!!!!!!!!!!! ///////////////////////////////////////////////////////////////////
// !!!!!!!!!!!!!!!!!!!!!!!!!! ///////////////////////////////////////////////////////////////////
package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static com.pedropathing.ivy.Scheduler.schedule;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redFortyEightStartingPose;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.routines.RedFortyEightRoutine;
import org.firstinspires.ftc.teamcode.subsystems.FollowerStorage;

@Autonomous
public class RedFortyEightAuto extends OpMode {
    private Follower follower;


    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(redFortyEightStartingPose);
        follower.update();

    }

    @Override
    public void start() {

        schedule(RedFortyEightRoutine.mainAuto(follower));
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();

        telemetry.update();
    }

    @Override
    public void stop() {
        FollowerStorage.autonomousEndPose = follower.pose();
    }
}

