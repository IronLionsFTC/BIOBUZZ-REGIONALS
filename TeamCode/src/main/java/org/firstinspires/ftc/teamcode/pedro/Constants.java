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
        c.xPodOffset.set(7.154774102639026);
        c.yPodOffset.set(-3.4528741310900593);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });


    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                c.maxVelocityConstraint.set(80.0);

                Controller primaryTranslationalForward = Controller.proportional(0.16392975708956303);
                Controller secondaryTranslationalForward = Controller.proportional(0.06056766496081127);
                Controller primaryTranslationalLateral = Controller.proportional(0.2470550831993693);
                Controller secondaryTranslationalLateral = Controller.proportional(0.09128025180876355);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.008780362996196901));
                c.brake.set(Controller.proportionalFeedforward(0.007463308546767366));

                c.headingFeedback.set(Controller.proportional(3.2796462411932246));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.042909367359536085, 0.008782870863269556));

                c.linearBrakeCoefficients.set(Matrix.diag(0.05598289243794019, 0.043874248658634256));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0015234296637561373, 0.001998349689168628));

                c.maxAchievableForwardVelocity.set(97.99509816357178);
                c.maxAchievableStrafeVelocity.set(89.88527765122369);
                c.naturalForwardDeceleration.set(28.49853860792421);
                c.naturalStrafeDeceleration.set(41.836478230531824);
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


