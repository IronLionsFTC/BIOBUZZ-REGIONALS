package org.firstinspires.ftc.teamcode.subsystems;


// =========================================
// The purpose of this is to
// allow the robot to aim at
// two buckets when toggled
// depending on if its North or
// South field.
//
// Y position > 0  -> North bucket
// Y position < 0  -> South bucket
//
// Only works while toggled.
//
// This is an addon to the existing GoalAim
// class and does not replace it.
// =========================================


import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class PositionDependentAiming {

    private final Follower follower;
    private final GoalAim goalAim;

    private boolean active = false;

    // =========================================
    // CHANGE THESE TO YOUR ACTUAL BUCKET
    // COORDINATES
    // =========================================

    private final double northTargetX = 0;
    private final double northTargetY = 90;

    private final double southTargetX = 0;
    private final double southTargetY = -90;


    // =========================================
    // Constructor
    // =========================================

    public PositionDependentAiming(Follower follower) {

        this.follower = follower;

        goalAim = new GoalAim(
                follower,
                northTargetX,
                northTargetY
        );
    }


    // =========================================
    // Toggle aiming on/off
    // =========================================

    public void toggle() {
        active = !active;
    }


    public void enable() {
        active = true;
    }


    public void disable() {
        active = false;
    }


    public boolean isActive() {
        return active;
    }


    // =========================================
    // Update which target GoalAim should use
    // based on the robot's Y position
    // =========================================

    private void updateTarget() {

        Pose robotPose = follower.pose();

        if (robotPose.y() >= 0) {

            // North side
            goalAim.setTarget(
                    northTargetX,
                    northTargetY
            );

        } else {

            // South side
            goalAim.setTarget(
                    southTargetX,
                    southTargetY
            );
        }
    }


    // =========================================
    // Get the turn power
    // =========================================

    public double getTurnPower() {

        if (!active) {
            return 0;
        }

        updateTarget();

        return goalAim.getTurnPower();
    }


    // =========================================
    // Access existing GoalAim information
    // =========================================

    public double getHeadingError() {

        if (!active) {
            return 0;
        }

        updateTarget();

        return goalAim.getHeadingError();
    }
}
