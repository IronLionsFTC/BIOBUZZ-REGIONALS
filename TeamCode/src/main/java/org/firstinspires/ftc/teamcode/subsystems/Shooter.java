
package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

@Config
public class Shooter {

    private final DcMotorEx motor1;
    private final DcMotorEx motor2;
    // No load is js used so it doesn't get above it and do weird things
    public static double TICKS_PER_REV = 28.0;
    public static double NO_LOAD_RPM = 5800.0;

    // KF adjusts for permanent dips or too high, it basically increases motor power
    public static double kF = 1.1;
    public static double kP = 0.001;

    private double targetRPM;
    private double currentRPM;

    public Shooter(HardwareMap hardwareMap) {

        motor1 = hardwareMap.get(DcMotorEx.class, "shooter1");
        motor2 = hardwareMap.get(DcMotorEx.class, "shooter2");

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        stop();
    }

    public void setTargetRPM(double rpm) {

        if (Double.isNaN(rpm) || Double.isInfinite(rpm)) {
            rpm = 0.0;
        }

        targetRPM = Range.clip(
                rpm,
                0.0,
                Math.max(1.0, NO_LOAD_RPM)
        );
    }

    public void update() {

        currentRPM = Math.abs(motor2.getVelocity())
                * 60.0 / Math.max(1.0, TICKS_PER_REV);

        double error = targetRPM - currentRPM;

        double power = kF * (targetRPM / Math.max(1.0, NO_LOAD_RPM))
                + kP * error;

        power = Range.clip(power, 0.0, 1.0);

        motor1.setPower(power);
        motor2.setPower(-power);
    }

    public double getCurrentRPM() {
        return currentRPM;
    }

    public double getTargetRPM() {
        return targetRPM;
    }

    public double getRPMError() {
        return targetRPM - currentRPM;
    }

    public double getTargetPower() {
        return targetRPM / Math.max(1.0, NO_LOAD_RPM);
    }

    public void stop() {

        targetRPM = 0.0;

        motor1.setPower(0.0);
        motor2.setPower(0.0);
    }
}
