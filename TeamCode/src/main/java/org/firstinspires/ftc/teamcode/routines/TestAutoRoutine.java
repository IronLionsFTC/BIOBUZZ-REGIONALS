package org.firstinspires.ftc.teamcode.routines;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.testAutoPath1;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.testAutoPath2;


public final class TestAutoRoutine {

    // Prevent creating instances of this class
    private TestAutoRoutine() {
    }


    // ==================== //
    //    MAIN AUTO ROUTINE
    // ==================== //

    public static Command mainAuto(Follower follower) {

        return sequential(
                follow(
                        follower,
                        testAutoPath1()
                ),

                follow(
                        follower,
                        testAutoPath2()
                )
        );
    }
}
