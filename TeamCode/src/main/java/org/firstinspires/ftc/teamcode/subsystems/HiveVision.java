
package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Vision pour identifier quel cote du HIVE rouge est en haut.
 * Utilise les clusters AprilTag BioBuzz du SDK FTC 12.0+.
 */
public final class HiveVision {

    public enum HiveSide {
        NORTHSIDEUP("northsideup"),
        SOUTHSIDEUP("southsideup"),
        UNKNOWN("unknown");

        private final String label;

        HiveSide(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static final double UPRIGHT_LIMIT_DEGREES = 75.0;
    private static final double INVERTED_LIMIT_DEGREES = 105.0;

    private final AprilTagProcessor aprilTagProcessor;
    private final VisionPortal visionPortal;

    private volatile String lastSummary = "Aucun cluster rouge detecte";
    private volatile int lastRedClusterCount = 0;
    private volatile HiveSide lastObservedSide = HiveSide.UNKNOWN;

    public HiveVision(HardwareMap hardwareMap) {
        this(hardwareMap, "Webcam 1");
    }

    public HiveVision(HardwareMap hardwareMap, String webcamName) {
        aprilTagProcessor = new AprilTagProcessor.Builder().build();
        aprilTagProcessor.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, webcamName))
                .addProcessor(aprilTagProcessor)
                .build();
    }

    /**
     * Analyse les detections les plus recentes.
     */
    public Observation observeCurrentFrame() {
        List<AprilTagDetection> detections =
                aprilTagProcessor.getDetections();

        Set<HiveSide> possibleSides =
                EnumSet.noneOf(HiveSide.class);

        List<String> summaries = new ArrayList<>();
        int redClusters = 0;

        for (AprilTagDetection detection : detections) {

            if (!(detection instanceof AprilTagClusterDetection)) {
                continue;
            }

            AprilTagClusterDetection cluster =
                    (AprilTagClusterDetection) detection;

            String clusterName = cluster.metadata.name == null
                    ? ""
                    : cluster.metadata.name.trim()
                    .toUpperCase(Locale.ROOT);

            HiveSide physicalSide =
                    sideForClusterName(clusterName);

            if (physicalSide == HiveSide.UNKNOWN) {
                continue;
            }

            redClusters++;

            double roll = detection.ftcPose == null
                    ? Double.NaN
                    : detection.ftcPose.roll;

            if (Double.isNaN(roll) || Double.isInfinite(roll)) {
                summaries.add(clusterName + " roll=indisponible");
                continue;
            }

            summaries.add(
                    String.format(
                            Locale.US,
                            "%s roll=%.1f deg",
                            clusterName,
                            roll
                    )
            );

            HiveSide estimate =
                    estimateSideUp(physicalSide, roll);

            if (estimate != HiveSide.UNKNOWN) {
                possibleSides.add(estimate);
            }
        }

        HiveSide result = HiveSide.UNKNOWN;

        if (possibleSides.size() == 1) {
            result = possibleSides.iterator().next();
        }

        lastRedClusterCount = redClusters;

        lastSummary = summaries.isEmpty()
                ? "Aucun cluster rouge BioBuzz visible"
                : String.join(" | ", summaries);

        lastObservedSide = result;

        return new Observation(
                result,
                redClusters,
                lastSummary
        );
    }

    public HiveSide getLastObservedSide() {
        return lastObservedSide;
    }

    public String getLastSummary() {
        return lastSummary;
    }

    public int getLastRedClusterCount() {
        return lastRedClusterCount;
    }

    public boolean isStreaming() {
        return visionPortal.getCameraState()
                == VisionPortal.CameraState.STREAMING;
    }

    public void close() {
        visionPortal.close();
    }

    private static HiveSide sideForClusterName(
            String clusterName) {

        // Hypothese : RED SCORING correspond au nord.
        if (clusterName.equals("RED SCORING")
                || clusterName.startsWith("RED SCORING ")) {
            return HiveSide.NORTHSIDEUP;
        }

        // Hypothese : RED AUDIENCE correspond au sud.
        if (clusterName.equals("RED AUDIENCE")
                || clusterName.startsWith("RED AUDIENCE ")) {
            return HiveSide.SOUTHSIDEUP;
        }

        return HiveSide.UNKNOWN;
    }

    private static HiveSide estimateSideUp(
            HiveSide tagSide,
            double rollDegrees) {

        double absoluteRoll = Math.abs(rollDegrees);

        if (absoluteRoll < UPRIGHT_LIMIT_DEGREES) {
            return tagSide;
        }

        if (absoluteRoll > INVERTED_LIMIT_DEGREES) {
            return tagSide == HiveSide.NORTHSIDEUP
                    ? HiveSide.SOUTHSIDEUP
                    : HiveSide.NORTHSIDEUP;
        }

        return HiveSide.UNKNOWN;
    }

    public static final class Observation {
        public final HiveSide side;
        public final int redClusterCount;
        public final String summary;

        private Observation(
                HiveSide side,
                int redClusterCount,
                String summary) {
            this.side = side;
            this.redClusterCount = redClusterCount;
            this.summary = summary;
        }
    }
}
