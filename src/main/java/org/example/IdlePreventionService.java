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

    private final MouseSimulator mouseSimulator;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public IdlePreventionService(int checkIntervalMs, int jiggleDistancePx) throws AWTException {
        this.checkIntervalMs = checkIntervalMs;
        this.jiggleDistancePx = jiggleDistancePx;
        this.mouseSimulator = new MouseSimulator();
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

    private void monitorIdleState() {
        Point lastMousePosition = mouseSimulator.getCurrentPosition();

        while (running.get()) {
            try {
                Thread.sleep(checkIntervalMs);
                interruptIfIdle(lastMousePosition);
                lastMousePosition = mouseSimulator.getCurrentPosition();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void interruptIfIdle(Point lastMousePosition) {
        Point currentPosition = mouseSimulator.getCurrentPosition();
        boolean isIdle = currentPosition.equals(lastMousePosition);

        if (isIdle) {
            mouseSimulator.jiggle(jiggleDistancePx);
            logger.info("Mouse jiggled at: " + new Date());
        }
    }
}
