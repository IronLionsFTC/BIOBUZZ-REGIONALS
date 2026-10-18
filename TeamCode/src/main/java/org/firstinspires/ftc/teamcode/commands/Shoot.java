package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.ShooterBlocker;

public class Shoot {

    private final Intake intake;
    private final ShooterBlocker shooterBlocker;
    private final ElapsedTime timer = new ElapsedTime();

    private static final double SHOOT_DURATION = 1.5;

    private boolean shooting = false;

    public Shoot(Intake intake, ShooterBlocker shooterBlocker) {
        this.intake = intake;
        this.shooterBlocker = shooterBlocker;
    }

    // Call this when the driver presses the shoot button.
    public void shoot() {
        if (shooting) {
            return;
        }

        shooting = true;

        shooterBlocker.open();
        intake.intake();

        timer.reset();
    }
    public void update() {
        if (!shooting) {
            return;
        }

        if (timer.seconds() >= SHOOT_DURATION) {
            intake.stop();
            shooterBlocker.close();

            shooting = false;
        }
    }

    public boolean isShooting() {
        return shooting;
    }

    public void cancel() {
        intake.stop();
        shooterBlocker.close();
        shooting = false;
    }
}