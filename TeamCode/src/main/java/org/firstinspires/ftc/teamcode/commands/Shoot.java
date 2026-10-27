package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterBlocker;

@Config
public class Shoot {

    public static double SHOOT_DURATION_SECONDS = 2.0;
    public static double FEED_PULSE_SECONDS = 0.18;
    public static double RECOVERY_MIN_SECONDS = 0.08;
    public static double MAX_READY_WAIT_SECONDS = 2.0;

    private enum State {
        IDLE,
        WAITING_FOR_READY,
        FEEDING,
        RECOVERING
    }

    private final Intake intake;
    private final ShooterBlocker shooterBlocker;
    private final Shooter shooter;

    private final ElapsedTime phaseTimer = new ElapsedTime();
    private final ElapsedTime burstTimer = new ElapsedTime();

    private State state = State.IDLE;

    public Shoot(
            Intake intake,
            ShooterBlocker shooterBlocker,
            Shooter shooter
    ) {

        this.intake = intake;
        this.shooterBlocker = shooterBlocker;
        this.shooter = shooter;
    }

    public void shoot() {

        if (state != State.IDLE) {
            return;
        }

        state = State.WAITING_FOR_READY;

        phaseTimer.reset();

        shooterBlocker.close();
        intake.stop();
    }

    public void update() {

        switch (state) {

            case IDLE:
                return;

            case WAITING_FOR_READY:

                shooterBlocker.close();
                intake.stop();

                if (shooter.isReady()) {

                    beginBurst();

                } else if (
                        phaseTimer.seconds()
                                >= Math.max(0.0, MAX_READY_WAIT_SECONDS)
                ) {

                    finish();
                }

                break;

            case FEEDING:

                if (burstTimer.seconds()
                        >= Math.max(0.0, SHOOT_DURATION_SECONDS)) {

                    finish();
                    return;
                }

                if (phaseTimer.seconds()
                        >= Math.max(0.02, FEED_PULSE_SECONDS)) {

                    shooterBlocker.close();

                    state = State.RECOVERING;
                    phaseTimer.reset();
                }

                break;

            case RECOVERING:

                if (burstTimer.seconds()
                        >= Math.max(0.0, SHOOT_DURATION_SECONDS)) {

                    finish();
                    return;
                }

                // Keep the intake running while the closed blockers hold
                // the next ball back from the shooter.
                intake.intake();
                shooterBlocker.close();

                if (phaseTimer.seconds()
                        >= Math.max(0.0, RECOVERY_MIN_SECONDS)
                        && shooter.isReady()) {

                    beginFeedPulse();
                }

                break;
        }
    }

    private void beginBurst() {

        burstTimer.reset();

        beginFeedPulse();
    }

    private void beginFeedPulse() {

        state = State.FEEDING;
        phaseTimer.reset();

        shooterBlocker.open();
        intake.intake();
    }

    private void finish() {

        state = State.IDLE;

        intake.stop();
        shooterBlocker.close();
    }

    public boolean isShooting() {
        return state != State.IDLE;
    }

    public void cancel() {
        finish();
    }
}