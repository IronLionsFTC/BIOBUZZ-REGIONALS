package org.firstinspires.ftc.teamcode.constants;

import com.pedropathing.paths.Path;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redBallSpike;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redCollectBallsInMiddle;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redCollectBallsOnWall;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redControlPointOfBallSpike;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redControlPointOfPark;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redFortyEightStartingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redNorthFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redNorthShootingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redPark;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redSouthShootingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redWaitForTilt;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.testAutoStart;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.testPoint;

public final class AutonomousPaths {

    // Prevent creating instances of this class
    private AutonomousPaths() {
    }


    // ==================== //
    //        PATHS
    // ==================== //

    public static Path redShootPreload() {
        return line(redFortyEightStartingPose, redSouthShootingPose)
                .linear(redFortyEightStartingPose, redSouthShootingPose);
    }

    public static Path redIntakeRedSpike() {
        return line(redSouthShootingPose, redBallSpike)
                .linear(redSouthShootingPose, redBallSpike);
    }

    public static Path redShootSpikeBalls() {
        return line(redBallSpike, redNorthShootingPose)
                .linear(redBallSpike, redNorthShootingPose);
    }

    public static Path redIntakeNorthFlower() {
        return line(redNorthShootingPose, redNorthFlower)
                .linear(redNorthShootingPose, redNorthFlower);
    }

    public static Path redShootNorthFlower() {
        return line(redNorthFlower, redNorthShootingPose)
                .linear(redNorthFlower, redNorthShootingPose);
    }

    public static Path redWaitForNorthTilt() {
        return line(redNorthShootingPose, redWaitForTilt)
                .linear(redNorthShootingPose, redWaitForTilt);
    }

    public static Path redCollectInitialNorthDroppedBalls() {
        return line(redWaitForTilt, redCollectBallsInMiddle)
                .linear(redWaitForTilt, redCollectBallsInMiddle);
    }

    public static Path redCollectFinalNorthDroppedBalls() {
        return line(redCollectBallsInMiddle, redCollectBallsOnWall)
                .linear(redCollectBallsInMiddle, redCollectBallsOnWall);
    }

    public static Path redReturnToPark() {
        return curve(redCollectBallsOnWall, redControlPointOfPark, redPark)
                .linear(redCollectBallsOnWall, redPark);
    }

    public static Path testAutoPath() {
        return line(testAutoStart, testPoint)
                .linear(testAutoStart, testPoint);
    }

}