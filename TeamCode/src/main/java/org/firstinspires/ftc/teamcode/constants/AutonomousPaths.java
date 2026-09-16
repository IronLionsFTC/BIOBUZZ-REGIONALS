package org.firstinspires.ftc.teamcode.constants;

import com.pedropathing.paths.Path;

import static com.pedropathing.api.Paths.line;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.northFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.northShootingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.redBallSpike;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.southFlower;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.southShootingPose;
import static org.firstinspires.ftc.teamcode.constants.AutonomousPoses.startPose;

public final class AutonomousPaths {

    // Prevent creating instances of this class
    private AutonomousPaths() {
    }


    // ==================== //
    //        PATHS
    // ==================== //

    public static Path shootPreload() {
        return line(startPose, southShootingPose)
                .linear(startPose, southShootingPose);
    }

    public static Path intakeSpike() {
        return line(southShootingPose, redBallSpike)
                .linear(southShootingPose, redBallSpike);
    }

    public static Path shootSpikeBalls() {
        return line(redBallSpike, northShootingPose)
                .linear(redBallSpike, northShootingPose);
    }

    public static Path intakeNorthFlower() {
        return line(northShootingPose, northFlower)
                .linear(northShootingPose, northFlower);
    }

    public static Path shootNorthFlower() {
        return line(northFlower, northShootingPose)
                .linear(northFlower, northShootingPose);
    }

    public static Path intakeSecondFlower() {
        return line(northShootingPose, southFlower)
                .linear(northShootingPose, southFlower);
    }

    public static Path shootSecondFlower() {
        return line(southFlower, southShootingPose)
                .linear(southFlower, southShootingPose);
    }
}