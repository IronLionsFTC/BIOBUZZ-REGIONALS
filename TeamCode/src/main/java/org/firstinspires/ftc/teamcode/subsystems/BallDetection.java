/// ALL BELOW CODE IS AI WRITTEN AS A TEST ///
package org.firstinspires.ftc.teamcode.subsystems;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class BallDetection implements VisionProcessor {

    // ============================================================
    // SETTINGS
    // ============================================================

    // Your tuned grayscale range
    private int lowerGray = 54;
    private int upperGray = 121;

    // Minimum / maximum ball radius in pixels
    private double minRadius = 10;
    private double maxRadius = 250;

    // Minimum contour area
    private double minArea = 100;

    // Circularity threshold
    // 1.0 = perfect circle
    // Lower values allow less-perfect circles.
    private double minCircularity = 0.55;

    // Minimum percentage of pixels inside the bounding
    // circle which must be inside our grayscale range.
    private double minFillRatio = 0.40;

    // ============================================================
    // CAMERA / OPENCV MATRICES
    // ============================================================

    private final Mat gray = new Mat();
    private final Mat blurred = new Mat();
    private final Mat mask = new Mat();
    private final Mat hierarchy = new Mat();

    // ============================================================
    // DETECTION RESULTS
    // ============================================================

    private volatile boolean detected = false;

    private volatile double ballX = 0;
    private volatile double ballY = 0;
    private volatile double ballRadius = 0;

    private volatile double confidence = 0;

    private int frameWidth = 0;
    private int frameHeight = 0;

    // ============================================================
    // DRAWING
    // ============================================================

    private final Paint circlePaint = new Paint();
    private final Paint textPaint = new Paint();

    @Override
    public void init(
            int width,
            int height,
            CameraCalibration calibration) {

        frameWidth = width;
        frameHeight = height;

        circlePaint.setColor(Color.GREEN);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(4);

        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(32);
        textPaint.setStyle(Paint.Style.FILL);
    }

    // ============================================================
    // MAIN VISION PROCESSING
    // ============================================================

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {

        // --------------------------------------------------------
        // 1. Convert camera image to grayscale
        // --------------------------------------------------------

        Imgproc.cvtColor(
                frame,
                gray,
                Imgproc.COLOR_RGB2GRAY
        );

        // --------------------------------------------------------
        // 2. Blur slightly to remove camera noise
        // --------------------------------------------------------

        Imgproc.GaussianBlur(
                gray,
                blurred,
                new Size(5, 5),
                0
        );

        // --------------------------------------------------------
        // 3. Threshold grayscale
        //
        // Pixels between 54 and 121 become white.
        // Everything else becomes black.
        // --------------------------------------------------------

        Core.inRange(
                blurred,
                new Scalar(lowerGray),
                new Scalar(upperGray),
                mask
        );

        // --------------------------------------------------------
        // 4. Morphological cleanup
        // --------------------------------------------------------

        Mat kernel = Imgproc.getStructuringElement(
                Imgproc.MORPH_ELLIPSE,
                new Size(5, 5)
        );

        Imgproc.morphologyEx(
                mask,
                mask,
                Imgproc.MORPH_OPEN,
                kernel
        );

        Imgproc.morphologyEx(
                mask,
                mask,
                Imgproc.MORPH_CLOSE,
                kernel
        );

        kernel.release();

        // --------------------------------------------------------
        // 5. Find contours
        // --------------------------------------------------------

        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(
                mask,
                contours,
                hierarchy,
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE
        );

        // --------------------------------------------------------
        // 6. Find best spherical candidate
        // --------------------------------------------------------

        double bestScore = 0;

        double bestX = 0;
        double bestY = 0;
        double bestRadius = 0;

        boolean found = false;

        for (MatOfPoint contour : contours) {

            double area = Imgproc.contourArea(contour);

            // Reject tiny objects
            if (area < minArea) {
                contour.release();
                continue;
            }

            // ----------------------------------------------------
            // Calculate perimeter
            // ----------------------------------------------------

            double perimeter = Imgproc.arcLength(
                    new org.opencv.core.MatOfPoint2f(
                            contour.toArray()
                    ),
                    true
            );

            if (perimeter <= 0) {
                contour.release();
                continue;
            }

            // ----------------------------------------------------
            // Circularity
            //
            // 4πA / P²
            //
            // Perfect circle = approximately 1
            // ----------------------------------------------------

            double circularity =
                    (4.0 * Math.PI * area) /
                            (perimeter * perimeter);

            if (circularity < minCircularity) {
                contour.release();
                continue;
            }

            // ----------------------------------------------------
            // Minimum enclosing circle
            // ----------------------------------------------------

            org.opencv.core.MatOfPoint2f contour2f =
                    new org.opencv.core.MatOfPoint2f(
                            contour.toArray()
                    );

            Point center = new Point();
            float[] radiusArray = new float[1];

            Imgproc.minEnclosingCircle(
                    contour2f,
                    center,
                    radiusArray
            );

            contour2f.release();

            double radius = radiusArray[0];

            // Reject objects that are too small or large
            if (radius < minRadius || radius > maxRadius) {
                contour.release();
                continue;
            }

            // ----------------------------------------------------
            // Fill ratio
            //
            // How much of the enclosing circle is actually
            // occupied by our thresholded object?
            // ----------------------------------------------------

            double circleArea =
                    Math.PI * radius * radius;

            double fillRatio =
                    area / circleArea;

            if (fillRatio < minFillRatio) {
                contour.release();
                continue;
            }

            // ----------------------------------------------------
            // Score candidate
            //
            // Circularity + fill + size
            // ----------------------------------------------------

            double score =
                    circularity *
                            fillRatio *
                            Math.min(radius / 50.0, 2.0);

            if (score > bestScore) {

                bestScore = score;

                bestX = center.x;
                bestY = center.y;
                bestRadius = radius;

                found = true;
            }

            contour.release();
        }

        // --------------------------------------------------------
        // 7. Store result
        // --------------------------------------------------------

        detected = found;

        if (found) {

            ballX = bestX;
            ballY = bestY;
            ballRadius = bestRadius;

            // Clamp confidence to 0-1
            confidence = Math.min(bestScore, 1.0);

        } else {

            ballX = 0;
            ballY = 0;
            ballRadius = 0;
            confidence = 0;
        }

        return null;
    }

    // ============================================================
    // DRAW RESULTS ON CAMERA PREVIEW
    // ============================================================

    @Override
    public void onDrawFrame(
            Canvas canvas,
            int onscreenWidth,
            int onscreenHeight,
            float scaleBmpPxToCanvasPx,
            float scaleCanvasDensity,
            Object userContext) {

        if (!detected) {
            return;
        }

        float x =
                (float) (ballX * scaleBmpPxToCanvasPx);

        float y =
                (float) (ballY * scaleBmpPxToCanvasPx);

        float radius =
                (float) (ballRadius * scaleBmpPxToCanvasPx);

        // Draw circle around ball
        canvas.drawCircle(
                x,
                y,
                radius,
                circlePaint
        );

        // Draw center point
        canvas.drawCircle(
                x,
                y,
                5,
                circlePaint
        );

        // Draw information
        canvas.drawText(
                String.format(
                        "Ball X: %.0f Y: %.0f R: %.0f",
                        ballX,
                        ballY,
                        ballRadius
                ),
                20,
                40,
                textPaint
        );
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public boolean isDetected() {
        return detected;
    }

    public double getX() {
        return ballX;
    }

    public double getY() {
        return ballY;
    }

    public double getRadius() {
        return ballRadius;
    }

    public double getConfidence() {
        return confidence;
    }

    // ============================================================
    // SETTINGS
    // ============================================================

    public void setGrayRange(int lower, int upper) {

        lowerGray = Math.min(lower, upper);
        upperGray = Math.max(lower, upper);
    }

    public void setRadiusRange(
            double minimum,
            double maximum) {

        minRadius = minimum;
        maxRadius = maximum;
    }

    public void setMinCircularity(double value) {
        minCircularity = value;
    }

    public void setMinFillRatio(double value) {
        minFillRatio = value;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }
}


