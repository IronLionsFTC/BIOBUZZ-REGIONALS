package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.DriveConstants.KP_POWER;
import static org.firstinspires.ftc.teamcode.constants.DriveConstants.MAX_TURN_POWER;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class GoalAim {

    private final Follower follower;

    // Target point you want to aim towards
    private double targetX;
    private double targetY;

    // The magnitude u want to turn by
    private double kP = KP_POWER;

    // Maximum power the robot will turn
    private double maxTurnPower = MAX_TURN_POWER;

    // Whether the aiming is currently active
    private boolean active = false;

    public GoalAim(Follower follower, double targetX, double targetY) {
        this.follower = follower;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    // Simple functions to enable, disable and toggle
    public void enable() {
        active = true;
    }

    public void disable() {
        active = false;
    }

    public void toggle() {
        active = !active;
    }

    public boolean isActive() {
        return active;
    }


    // This calculates exatly the turn power needed, accounting for angle, KP, domain and maximum values
    public double getTurnPower() {

        if (!active) {
            return 0;
        }

        Pose robotPose = follower.pose();
        // Takes the wanted X position, and finds the vector (x+y) from the robot to the ideal point
        double dx = targetX - robotPose.x();
        double dy = targetY - robotPose.y();

        // Uses tan to find the target angle using the opposite and adjacent sides
        double targetHeading = Math.atan2(dy, dx);

        // Finds the difference between the target and current angle (but makes sure its within the domain first)
        double headingError = normalizeAngle(
                targetHeading - robotPose.heading()
        );

        // kp acts as the attack the robot gives it. the turn power decreases as the heading error decreases, making it slow down
        // kp is just a magnitude to it... a low KP turns items too slowly, whilst a fast KP might be too fast
        double turnPower = -headingError * kP;

        // Max turn power is 1, it looks at the minimum number between the target turn power and the max
        // if the max power is above 1, it will choose the turn power
        turnPower = Math.max(
                -maxTurnPower,
                Math.min(maxTurnPower, turnPower)
        );

        return turnPower;
    }

    // This is incase the user wants to see how much heading error there is within their OpMode
    public double getHeadingError() {

        Pose robotPose = follower.pose();

        double dx = targetX - robotPose.x();
        double dy = targetY - robotPose.y();

        double targetHeading = Math.atan2(dy, dx);

        return normalizeAngle(
                targetHeading - robotPose.heading()
        );
    }


    // A simple function to allow the user to set the target, and all values along with it (in OpMode itself)
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

    public void setKP(double kP) {
        this.kP = kP;
    }

    public void setMaxTurnPower(double maxTurnPower) {
        this.maxTurnPower = maxTurnPower;
    }

    //This makes sure the angle stays between -pi and +pi, this is so it can actually turn to that point.
    private double normalizeAngle(double angle) {

        while (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }

        while (angle < -Math.PI) {
            angle += 2 * Math.PI;
        }

        return angle;
    }
}