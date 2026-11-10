package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.AIM_DEADBAND_DEGREES;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.KP_POWER;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.KD_POWER;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.MAX_TURN_POWER;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.MIN_TURN_POWER;


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.math.AimingTargetMath;


/*
 * =========================================
 * Position Dependent Goal Aiming
 * =========================================
 *
 * Automatically aims at:
 *
 *      Y >= 68 -> North goal
 *      Y <  68 -> South goal
 *
 * The robot can drive while aiming
 *
 *
 *      Turn Power = KP * angle error
 *                   - KD * angular velocity --> calcluated from angle / time^2 basically
 *
 * KP makes the robot turn toward the target proportionally
 * KD slows the robot down as it is already rotating to prevent overshooting
 *
 * =========================================
 */

public class Aiming {

    private final Follower follower;
    private final AimingTargetMath aimingTargetCalculator;


    // =========================================
    // Define some items
    // =========================================

    private double lastHeading = 0.0;
    private long lastTime = 0;
    private boolean active = false;


    // =========================================
    // Constructor // Init
    // =========================================

    public Aiming(Follower follower) {

        this.follower = follower;
        this.aimingTargetCalculator = new AimingTargetMath(follower);

        resetController();
    }


    // ================================================
    // Toggle function to call in opmode
    // ================================================

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
    // Main aiming calculation
    // =========================================

    public double getTurnPower() {

        if (!active) {
            return 0.0;
        }

        // Update which target we're aiming at
        aimingTargetCalculator.updateTarget();

        Pose robotPose = follower.pose();

        // ==============================================================
        // Find the target heading 180 then normalise it and find error
        // ==================================================================


        // Desired heading towards target
        double targetHeading =
                aimingTargetCalculator.getAngleToTarget()
                        + Math.PI;

        // Shortest angular error [-PI, PI]
        double headingError = normalizeAngle(
                targetHeading - robotPose.heading()
        );

        // ===================================================
        // Deadband to prevent jittering
        // ===================================================

        double deadband = Math.toRadians(AIM_DEADBAND_DEGREES);

        if (Math.abs(headingError) <= deadband) {

            resetController();

            return 0.0;
        }

        // =========================================
        // change in time (FOR THE PID LOOP)
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
        // change in heading
        // =========================================

        double headingChange = normalizeAngle(
                robotPose.heading() - lastHeading
        );

        double angularVelocity =
                headingChange / deltaTime;

        lastHeading = robotPose.heading();

        // ======================================================
        // Uses above to get the KD working, and obv KP is simple
        // ======================================================

        double turnPower =
                (headingError * KP_POWER)
                        - (angularVelocity * KD_POWER);




        // ====================================================
        // Just a way to get a max and min to stop silly values
        // ====================================================

        double minimumPower = MIN_TURN_POWER;

        if (turnPower > 0) {

            turnPower = Math.max(turnPower, minimumPower);

        } else if (turnPower < 0) {

            turnPower = Math.min(turnPower, -minimumPower);
        }


        turnPower = Math.max(-MAX_TURN_POWER, Math.min(MAX_TURN_POWER, turnPower));

        return turnPower;
    }


    // =================================================================
    // Function to get the heading for telemetry normally or other math
    // =================================================================

    public double getHeadingError() {

        aimingTargetCalculator.updateTarget();

        Pose robotPose = follower.pose();

        double targetHeading =
                aimingTargetCalculator.getAngleToTarget()
                        + Math.PI;

        return normalizeAngle(
                targetHeading - robotPose.heading()
        );
    }

    // =================================================================
    // Reset controller memory <---- called alot, stops weird glitches
    // =================================================================

    private void resetController() {

        Pose robotPose = follower.pose();

        lastHeading = robotPose.heading();

        lastTime = System.nanoTime();
    }


    // =========================================================
    // Normalize angle to [-PI, PI] so it takes shortest path.
    // =========================================================

    private double normalizeAngle(double angle) {

        return Math.atan2(
                Math.sin(angle),
                Math.cos(angle)
        );
    }


}