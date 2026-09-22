//
//package org.firstinspires.ftc.teamcode.subsystems;
//
//import com.pedropathing.ivy.Command;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//public class Intake {
//
//    private final DcMotorEx motor;
//
//    public Intake(HardwareMap hardwareMap) {
//        motor = hardwareMap.get(DcMotorEx.class, "intake");
//    }
//
//
//
//    public Command intake() {
//        return Command.build()
//                .setExecute(() -> motor.setPower(1.0))
//                .setDone(() -> false)
//                .setEnd(endCondition -> motor.setPower(0))
//                .requiring(motor);
//    }
//
//    public Command reverse() {
//        return Command.build()
//                .setExecute(() -> motor.setPower(-1.0))
//                .setDone(() -> false)
//                .setEnd(endCondition -> motor.setPower(0))
//                .requiring(motor);
//    }
//
//    public Command stop() {
//        return Command.build()
//                .setStart(() -> motor.setPower(0))
//                .setDone(() -> true);
//    }
//}
