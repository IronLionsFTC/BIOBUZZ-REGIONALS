
package org.firstinspires.ftc.teamcode.opmodes.debug;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.BallVisionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

import android.util.Size;
@TeleOp(name = "Ball Vision Test", group = "Vision")
public class BallVisionTest extends LinearOpMode {



    // =============================================
    //    BALL VISION TEST IS PURELY EXPERIMENTAL
    // =============================================



    private VisionPortal visionPortal;
    private BallVisionProcessor ballProcessor;

    @Override
    public void runOpMode() {

        // --------------------------------------------------------
        // Create our custom vision processor
        // --------------------------------------------------------

        ballProcessor = new BallVisionProcessor();

        // --------------------------------------------------------
        // Create VisionPortal
        // --------------------------------------------------------

        visionPortal = new VisionPortal.Builder()

                // Your webcam must be configured as "Webcam 1"
                .setCamera(
                        hardwareMap.get(
                                WebcamName.class,
                                "Webcam 1"
                        )
                )

                // Start at 640x480
                .setCameraResolution(
                        new Size(640, 480)
                )

                .setStreamFormat(
                        VisionPortal.StreamFormat.MJPEG
                )

                // Add our ball detector
                .addProcessor(ballProcessor)

                // Show camera preview
                .enableLiveView(true)

                .build();

        telemetry.addLine("Camera starting...");
        telemetry.update();

        // --------------------------------------------------------
        // Wait until camera is actually streaming
        // --------------------------------------------------------

        while (
                !isStopRequested() &&
                        visionPortal.getCameraState()
                                != VisionPortal.CameraState.STREAMING
        ) {

            telemetry.addData(
                    "Camera",
                    visionPortal.getCameraState()
            );

            telemetry.update();

            sleep(20);
        }

        telemetry.addLine("Camera ready");
        telemetry.update();

        waitForStart();

        // --------------------------------------------------------
        // Main loop
        // --------------------------------------------------------

        while (opModeIsActive()) {

            telemetry.addData(
                    "Detected",
                    ballProcessor.isDetected()
            );

            telemetry.addData(
                    "X",
                    "%.1f",
                    ballProcessor.getX()
            );

            telemetry.addData(
                    "Y",
                    "%.1f",
                    ballProcessor.getY()
            );

            telemetry.addData(
                    "Radius",
                    "%.1f",
                    ballProcessor.getRadius()
            );

            telemetry.addData(
                    "Confidence",
                    "%.2f",
                    ballProcessor.getConfidence()
            );

            telemetry.addData(
                    "Frame",
                    "%dx%d",
                    ballProcessor.getFrameWidth(),
                    ballProcessor.getFrameHeight()
            );

            telemetry.update();

            sleep(20);
        }

        // --------------------------------------------------------
        // Close camera when OpMode ends
        // --------------------------------------------------------

        visionPortal.close();
    }
}


