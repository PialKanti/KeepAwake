package org.example;

import org.example.input.MouseSimulator;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class IdlePreventionService {
    private static final Logger logger = Logger.getLogger(IdlePreventionService.class.getName());

    private final int checkIntervalMs;
    private final int jiggleDistancePx;

    private long lastActivityTime;
    private final MouseSimulator mouseSimulator;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public IdlePreventionService(int checkIntervalMs, int jiggleDistancePx) throws AWTException {
        this.checkIntervalMs = checkIntervalMs;
        this.jiggleDistancePx = jiggleDistancePx;
        this.mouseSimulator = new MouseSimulator();
        this.lastActivityTime = System.currentTimeMillis();
    }

    public void start() {
        running.set(true);
        logger.info("KeepAwake started: interval = " + checkIntervalMs + "ms, distance = " + jiggleDistancePx + "px");

        Thread thread = new Thread(this::monitorIdleState, "KeepAwake-Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running.set(false);
    }

    public void updateActivity() {
        lastActivityTime = System.currentTimeMillis();
    }

    private void monitorIdleState() {
        while (running.get()) {
            try {
                Thread.sleep(checkIntervalMs);

                long idleTime = System.currentTimeMillis() - lastActivityTime;
                if (idleTime >= checkIntervalMs) {
                    stealthJiggle();
                    lastActivityTime = System.currentTimeMillis();
                    logger.info("Mouse jiggled at " + System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void stealthJiggle() {
        Point current = mouseSimulator.getCurrentPosition();
        int x = current.x;
        int y = current.y;

        // Tiny square jiggle
        mouseSimulator.move(x + jiggleDistancePx, y);
        mouseSimulator.move(x + jiggleDistancePx, y + jiggleDistancePx);
        mouseSimulator.move(x, y + jiggleDistancePx);
        mouseSimulator.move(x, y);
    }
}
