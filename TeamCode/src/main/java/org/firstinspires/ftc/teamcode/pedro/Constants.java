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

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(6.815895020492434);
        c.yPodOffset.set(-2.46768020269439);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.resetMode.set(PinpointLocalizer.ResetMode.RESET_AND_RECALIBRATE_IMU);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.17588293926386975);
                Controller secondaryTranslationalForward = Controller.proportional(0.06498404637930752);
                Controller primaryTranslationalLateral = Controller.proportional(0.2880843467764165);
                Controller secondaryTranslationalLateral = Controller.proportional(0.10643946837836843);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.00938266777271829));
                c.brake.set(Controller.proportionalFeedforward(0.007975267606810546));

                c.headingFeedback.set(Controller.proportional(3.598032667567826));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04150940415392835, 0.010072885575973918));

                c.linearBrakeCoefficients.set(Matrix.diag(0.08833723209273789, 0.038459419085413844));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0010838821355381057, 0.0019681937016639414));

                c.maxAchievableForwardVelocity.set(95.95486068876167);
                c.maxAchievableStrafeVelocity.set(81.21382665877638);
                c.naturalForwardDeceleration.set(31.327186717618623);
                c.naturalStrafeDeceleration.set(49.99313602436344);
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


