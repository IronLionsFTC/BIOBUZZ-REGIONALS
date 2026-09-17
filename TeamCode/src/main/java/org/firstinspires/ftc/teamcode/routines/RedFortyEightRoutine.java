package org.firstinspires.ftc.teamcode.routines;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redCollectFinalNorthDroppedBalls;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redCollectInitialNorthDroppedBalls;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redIntakeNorthFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redIntakeRedSpike;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redReturnToPark;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redShootNorthFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redShootPreload;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redShootSpikeBalls;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPaths.redWaitForNorthTilt;


public final class RedFortyEightRoutine {

    // Prevent creating instances of this class
    private RedFortyEightRoutine() {
    }


    // ==================== //
    //    MAIN AUTO ROUTINE
    // ==================== //

    public static Command mainAuto(Follower follower) {

        return sequential(

                // Shoot preload
                follow(
                        follower,
                        redShootPreload()
                ),

                // Intake spike
                deadline(
                        follow(
                                follower,
                                redIntakeRedSpike()
                        )
                        // Add intake command here
                ),

                // Shoot spike balls
                follow(
                        follower,
                        redShootSpikeBalls()
                ),

                // Intake north flower
                deadline(
                        follow(
                                follower,
                                redIntakeNorthFlower()
                        )
                        // Intake command
                ),

                // Shoot north flower
                follow(
                        follower,
                        redShootNorthFlower()
                ),

                // Wait for North to tip
                deadline(
                        follow(
                                follower,
                                redWaitForNorthTilt()
                        )
                        // Intake command
                ),

                // Collect Dropped Pollen
                deadline(
                    follow(
                                follower,
                                redCollectInitialNorthDroppedBalls()
                        )
                        // Intake command
                ),

                deadline(
                        follow(
                                follower,
                                redCollectFinalNorthDroppedBalls()
                        )
                        // Intake command
                ),

                follow(
                        follower,
                        redReturnToPark()

                )
        );
    }
}
