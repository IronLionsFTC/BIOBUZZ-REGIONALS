package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.AIM_DEADBAND_DEGREES;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.KP_POWER;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.KD_POWER;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.MAX_TURN_POWER;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.MIN_TURN_POWER;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_MIDDLE_POINT_Y;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_NORTH_TARGET_X;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_NORTH_TARGET_Y;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_SOUTH_TARGET_X;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.RED_SOUTH_TARGET_Y;


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;


/*
 * =========================================
 * Position Dependent Goal Aiming
 * =========================================
 *
 * Automatically aims at:
 *
 *      Y >= 0 -> North target
 *      Y <  0 -> South target
 *
 * The robot can drive while aiming.
 *
 * The controller uses:
 *
 *      Turn Power = KP * angle error
 *                   - KD * angular velocity
 *
 * KP makes the robot turn toward the target.
 * KD slows the robot down as it is already rotating.
 *
 * This prevents the large oscillations that happen
 * when the derivative is calculated from heading error
 * while the target angle is also changing.
 *
 * =========================================
 */

public class PositionDependentAiming {

    private final Follower follower;

    // =========================================
    // Targets
    // =========================================

    private double targetX;
    private double targetY;



    // =========================================
    // Controller state
    // =========================================

    private double lastHeading = 0.0;
    private long lastTime = 0;

    private boolean active = false;


    // =========================================
    // Constructor
    // =========================================

    public PositionDependentAiming(Follower follower) {

        this.follower = follower;

        // Default target.
        targetX = RED_NORTH_TARGET_X;
        targetY = RED_NORTH_TARGET_Y;

        resetController();
    }


    // =========================================
    // Enable / Disable / Toggle
    // =========================================

    public void enable() {

        active = true;

        // Reset controller memory so enabling aim does
        // not create a massive derivative spike.
        resetController();
    }


    public void disable() {

        active = false;

        resetController();
    }


    public void toggle() {

        if (active) {
            disable();
        } else {
            enable();
        }
    }


    public boolean isActive() {

        return active;
    }


    // =========================================
    // Select North or South target
    // =========================================

    private void updateTarget() {

        Pose robotPose = follower.pose();


        if (robotPose.y() >= RED_MIDDLE_POINT_Y) {

            targetX = RED_NORTH_TARGET_X;
            targetY = RED_NORTH_TARGET_Y;

        } else {

            targetX = RED_SOUTH_TARGET_X;
            targetY = RED_SOUTH_TARGET_Y;
        }
    }


    // =========================================
    // Main aiming calculation
    // =========================================

    public double getTurnPower() {

        if (!active) {
            return 0.0;
        }

        // Update which target we are aiming at
        updateTarget();

        Pose robotPose = follower.pose();

        // =========================================
        // Calculate vector from robot -> target
        // =========================================

        double dx = targetX - robotPose.x();
        double dy = targetY - robotPose.y();

        // Desired heading towards target
        double targetHeading = Math.atan2(dy, dx) + Math.PI;

        // Shortest angular error [-PI, PI]
        double headingError = normalizeAngle(
                targetHeading - robotPose.heading()
        );

        // =========================================
        // Deadband
        // =========================================

        double deadband = Math.toRadians(AIM_DEADBAND_DEGREES);

        if (Math.abs(headingError) <= deadband) {

            resetController();

            return 0.0;
        }

        // =========================================
        // Calculate timestep
        // =========================================

        long currentTime = System.nanoTime();

        double deltaTime;

        if (lastTime == 0) {
            deltaTime = 0.02;
        } else {
            deltaTime =
                    (currentTime - lastTime)
                            / 1_000_000_000.0;
        }

        lastTime = currentTime;

        // Prevent ridiculous timestep values
        deltaTime = Math.max(
                0.005,
                Math.min(deltaTime, 0.1)
        );

        // =========================================
        // Calculate actual angular velocity
        // =========================================

        double headingChange = normalizeAngle(
                robotPose.heading() - lastHeading
        );

        double angularVelocity =
                headingChange / deltaTime;

        lastHeading = robotPose.heading();

        // =========================================
        // PD controller
        // =========================================

        double turnPower =
                (headingError * KP_POWER)
                        - (angularVelocity * KD_POWER);

        // =========================================
        // Minimum power to overcome friction
        // =========================================
        //
        // If the controller wants to turn but the calculated
        // power is too small to physically move the robot,
        // force it up to the minimum useful power.
        //
        // IMPORTANT:
        // We only do this when turnPower != 0.
        // The sign is preserved.
        // =========================================

        double minimumPower = MIN_TURN_POWER;

        if (turnPower > 0) {

            turnPower = Math.max(
                    turnPower,
                    minimumPower
            );

        } else if (turnPower < 0) {

            turnPower = Math.min(
                    turnPower,
                    -minimumPower
            );
        }

        // =========================================
        // Maximum power limit
        // =========================================

        turnPower = Math.max(
                -MAX_TURN_POWER,
                Math.min(MAX_TURN_POWER, turnPower)
        );

        return turnPower;
    }


    // =========================================
    // Return current heading error
    // =========================================

    public double getHeadingError() {

        updateTarget();

        Pose robotPose = follower.pose();

        double dx = targetX - robotPose.x();
        double dy = targetY - robotPose.y();

        double targetHeading = Math.atan2(dy, dx) + Math.PI;

        return normalizeAngle(
                targetHeading - robotPose.heading()
        );
    }


    // =========================================
    // Manually change target
    // =========================================

    public void setTarget(double x, double y) {

        targetX = x;
        targetY = y;
    }


    public double getTargetX() {

        return targetX;
    }


    public double getTargetY() {

        return targetY;
    }




    // =========================================
    // Reset controller memory
    // =========================================

    private void resetController() {

        Pose robotPose = follower.pose();

        lastHeading = robotPose.heading();

        lastTime = System.nanoTime();
    }


    // =========================================
    // Normalize angle to [-PI, PI]
    // =========================================

    private double normalizeAngle(double angle) {

        return Math.atan2(
                Math.sin(angle),
                Math.cos(angle)
        );
    }


}
