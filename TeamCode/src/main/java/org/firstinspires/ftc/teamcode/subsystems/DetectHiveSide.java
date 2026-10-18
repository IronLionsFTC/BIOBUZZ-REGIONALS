/// ALL CODE IS AI WRITTEN AS A TEST ///
package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.ivy.Command;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Commande Ivy qui analyse plusieurs images,
 * puis choisit le cote du HIVE.
 */
public final class DetectHiveSide {

    public static final int DEFAULT_SCAN_COUNT = 10;
    public static final long DEFAULT_SCAN_INTERVAL_MS = 100;

    private final HiveVision hiveVision;
    private final Telemetry telemetry;

    private volatile HiveVision.HiveSide result =
            HiveVision.HiveSide.UNKNOWN;

    private volatile boolean fallbackUsed = false;
    private volatile int completedScans = 0;
    private volatile int northVotes = 0;
    private volatile int southVotes = 0;

    public DetectHiveSide(HiveVision hiveVision) {
        this(hiveVision, null);
    }

    public DetectHiveSide(
            HiveVision hiveVision,
            Telemetry telemetry) {

        if (hiveVision == null) {
            throw new IllegalArgumentException(
                    "hiveVision ne doit pas etre null"
            );
        }

        this.hiveVision = hiveVision;
        this.telemetry = telemetry;
    }

    public Command scanCommand() {
        return scanCommand(
                DEFAULT_SCAN_COUNT,
                DEFAULT_SCAN_INTERVAL_MS
        );
    }

    public Command scanCommand(
            int scanCount,
            long intervalMs) {

        if (scanCount < 1) {
            throw new IllegalArgumentException(
                    "scanCount doit etre au moins 1"
            );
        }

        if (intervalMs < 1) {
            throw new IllegalArgumentException(
                    "intervalMs doit etre au moins 1 ms"
            );
        }

        final ScanRun run = new ScanRun();
        final long intervalNanos = intervalMs * 1_000_000L;

        return Command.build()
                .setStart(() -> {
                    run.scans = 0;
                    run.northVotes = 0;
                    run.southVotes = 0;
                    run.nextScanTimeNanos = 0L;

                    result = HiveVision.HiveSide.UNKNOWN;
                    fallbackUsed = false;
                    completedScans = 0;
                    northVotes = 0;
                    southVotes = 0;
                })
                .setExecute(() -> {
                    long now = System.nanoTime();

                    if (now < run.nextScanTimeNanos) {
                        return;
                    }

                    run.nextScanTimeNanos =
                            now + intervalNanos;

                    HiveVision.Observation observation =
                            hiveVision.observeCurrentFrame();

                    run.scans++;

                    if (observation.side
                            == HiveVision.HiveSide.NORTHSIDEUP) {
                        run.northVotes++;
                    } else if (observation.side
                            == HiveVision.HiveSide.SOUTHSIDEUP) {
                        run.southVotes++;
                    }

                    completedScans = run.scans;
                    northVotes = run.northVotes;
                    southVotes = run.southVotes;

                    if (telemetry != null) {
                        telemetry.addData(
                                "HIVE scan",
                                "%d/%d",
                                run.scans,
                                scanCount
                        );

                        telemetry.addData(
                                "HIVE visible",
                                observation.summary
                        );

                        telemetry.addData(
                                "HIVE estimate",
                                observation.side
                        );

                        telemetry.addData(
                                "HIVE votes",
                                "north=%d south=%d",
                                run.northVotes,
                                run.southVotes
                        );

                        telemetry.update();
                    }

                    if (run.scans >= scanCount) {
                        resolveResult(run);
                    }
                })
                .setDone(() -> run.scans >= scanCount);
    }

    private void resolveResult(ScanRun run) {
        int validVotes =
                run.northVotes + run.southVotes;

        // Aucun resultat fiable : le chemin par defaut est le nord.
        fallbackUsed = validVotes == 0
                || run.northVotes == run.southVotes;

        if (run.southVotes > run.northVotes) {
            result = HiveVision.HiveSide.SOUTHSIDEUP;
        } else {
            result = HiveVision.HiveSide.NORTHSIDEUP;
        }

        if (telemetry != null) {
            telemetry.addData("HIVE final", result);
            telemetry.addData("HIVE fallback", fallbackUsed);
            telemetry.update();
        }
    }

    public HiveVision.HiveSide getResult() {
        return result;
    }

    public boolean wasFallbackUsed() {
        return fallbackUsed;
    }

    public int getCompletedScans() {
        return completedScans;
    }

    public int getNorthVotes() {
        return northVotes;
    }

    public int getSouthVotes() {
        return southVotes;
    }

    private static final class ScanRun {
        int scans;
        int northVotes;
        int southVotes;
        long nextScanTimeNanos;
    }
}
