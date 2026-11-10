package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.IntakeTransfer;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.ShooterBlocker;

@Config
public class Shoot {

    private final IntakeTransfer intake;
    private final ShooterBlocker shooterBlocker;

    public Shoot(
            IntakeTransfer intake,
            ShooterBlocker shooterBlocker
    ) {

        this.intake = intake;
        this.shooterBlocker = shooterBlocker;
    }

    public void shoot() {

        shooterBlocker.open();
        intake.run();
    }

    public void noShoot(){
        shooterBlocker.close();
        intake.stop();
    }

}