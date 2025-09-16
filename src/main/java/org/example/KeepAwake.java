package org.example;

import org.example.input.MouseSimulator;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class KeepAwake {
    private static final Logger logger = Logger.getLogger(KeepAwake.class.getName());

    private final int idleCheckIntervalMillis;
    private final int moveDistance;

    private final MouseSimulator mouseSimulator;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public KeepAwake(int idleCheckIntervalMillis, int moveDistance) throws AWTException {
        this.idleCheckIntervalMillis = idleCheckIntervalMillis;
        this.moveDistance = moveDistance;
        this.mouseSimulator = new MouseSimulator();
    }

    public void start() {
        running.set(true);
        logger.info("KeepAwake started: interval = " + idleCheckIntervalMillis + "ms, distance = " + moveDistance + "px");

        new Thread(() -> {
            Point lastMousePosition = mouseSimulator.getCurrentPosition();

            while (running.get()) {
                try {
                    Thread.sleep(idleCheckIntervalMillis);
                    Point currentPosition = mouseSimulator.getCurrentPosition();

                    boolean isIdle = currentPosition.equals(lastMousePosition);

                    if (isIdle) {
                        mouseSimulator.jiggle(moveDistance);
                        logger.info("Mouse jiggled at: " + new Date());
                    }

                    lastMousePosition = mouseSimulator.getCurrentPosition();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "KeepAwake-Thread").start();
    }

    public void stop() {
        running.set(false);
    }
}
