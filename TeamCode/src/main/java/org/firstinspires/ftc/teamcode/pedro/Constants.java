package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeft");
        c.frontRightName.set("frontRight");
        c.backLeftName.set("backLeft");
        c.backRightName.set("backRight");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.manualBrakeMode.set(true);
    });
    public static PinpointConfig localizerConfig = new PinpointConfig(
            c -> {
                c.name.set("pinpoint");

                // KEEP THESE AS YOUR ACTUAL MEASURED POD OFFSETS
                c.xPodOffset.set(6.01);
                c.yPodOffset.set(-6.02);

                c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
                c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
            }
    );

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                // Translational control
                Controller primaryTranslationalForward =
                        Controller.proportional(0.3);
                Controller secondaryTranslationalForward =
                        Controller.proportional(0.1);

                Controller primaryTranslationalLateral =
                        Controller.proportional(0.3);
                Controller secondaryTranslationalLateral =
                        Controller.proportional(0.1);

                c.forwardTranslational.set(
                        Controller.piecewise(secondaryTranslationalForward)
                                .put(2.5, primaryTranslationalForward)
                );

                c.strafeTranslational.set(
                        Controller.piecewise(secondaryTranslationalLateral)
                                .put(2.5, primaryTranslationalLateral)
                );

                // Basic feedforward
                c.coast.set(
                        Controller.proportionalFeedforward(0.01)
                );

                c.brake.set(
                        Controller.proportionalFeedforward(0.01)
                );

                // Heading control
                c.headingFeedback.set(
                        Controller.proportional(5.0)
                );

                c.headingBrakeCoefficients.set(
                        Vector2D.cartesian(0.05, 0.006)
                );

                // Linear braking
                c.linearBrakeCoefficients.set(
                        Matrix.diag(0.1, 0.1)
                );

                // Quadratic braking
                c.quadraticBrakeCoefficients.set(
                        Matrix.diag(0.0015, 0.0015)
                );

                // Conservative velocity estimates
                c.maxAchievableForwardVelocity.set(70.0);
                c.maxAchievableStrafeVelocity.set(50.0);

                c.naturalForwardDeceleration.set(85.0);
                c.naturalStrafeDeceleration.set(100.0);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }


}