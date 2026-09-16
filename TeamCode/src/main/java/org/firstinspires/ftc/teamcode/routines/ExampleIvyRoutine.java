package org.firstinspires.ftc.teamcode.routines;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.constants.AutonomousPaths;

public final class ExampleIvyRoutine {

    // Prevent creating instances of this class
    private ExampleIvyRoutine() {
    }


    // ==================== //
    //    MAIN AUTO ROUTINE
    // ==================== //

    public static Command mainAuto(Follower follower) {

        return sequential(

                // Shoot preload
                follow(
                        follower,
                        AutonomousPaths.shootPreload()
                ),

                // Intake spike
                deadline(
                        follow(
                                follower,
                                AutonomousPaths.intakeSpike()
                        )
                        // Add intake command here
                ),

                // Shoot spike balls
                follow(
                        follower,
                        AutonomousPaths.shootSpikeBalls()
                ),

                // Intake north flower
                deadline(
                        follow(
                                follower,
                                AutonomousPaths.intakeNorthFlower()
                        )
                        // Add intake command here
                ),

                // Shoot north flower
                follow(
                        follower,
                        AutonomousPaths.shootNorthFlower()
                ),

                // Intake second flower
                deadline(
                        follow(
                                follower,
                                AutonomousPaths.intakeSecondFlower()
                        )
                        // Add intake command here
                ),

                // Shoot second flower
                follow(
                        follower,
                        AutonomousPaths.shootSecondFlower()
                )
        );
    }
}
