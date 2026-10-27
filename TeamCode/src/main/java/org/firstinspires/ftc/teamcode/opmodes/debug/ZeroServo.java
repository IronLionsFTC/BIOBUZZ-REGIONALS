
package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

@TeleOp
public class ZeroServo extends OpMode {
    public Servo servo1;
    public Servo servo2;

    public void init(){
        servo1 = hardwareMap.get(Servo.class, "pollenHood");
        servo2 = hardwareMap.get(Servo.class, "nectarHood");



    }

    @Override
    public void loop(){
        servo1.setPosition(0);
        servo2.setPosition(0);
        telemetry.addData("Servo 1 Pos:", servo1.getPosition());
        telemetry.addData("Servo 2 Pos:", servo2.getPosition());













    }

}

