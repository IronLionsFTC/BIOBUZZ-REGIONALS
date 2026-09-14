package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

@TeleOp
public class OdoDebug extends OpMode {
    public DcMotor odoPod;
    public Integer position;

    public void init(){
        odoPod = (DcMotor) hardwareMap.get(DeviceConfiguration.I2cChannel.class, "pinpoint");



    }

    @Override
    public void loop(){
        position = odoPod.getCurrentPosition();
        telemetry.addData("Pinpoint", position);












    }

}
