package org.example;

import java.awt.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class KeepAwake {
    private static final Logger logger = Logger.getLogger(KeepAwake.class.getName());

    private final int idleCheckIntervalMillis;
    private final int moveDistance;
    private final Robot robot;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public KeepAwake(int idleCheckIntervalMillis, int moveDistance) throws AWTException {
        this.idleCheckIntervalMillis = idleCheckIntervalMillis;
        this.moveDistance = moveDistance;
        this.robot = new Robot();
    }

    public void start() {
        running.set(true);
        logger.info("KeepAwake started: interval={}ms, distance={}px" + idleCheckIntervalMillis + "ms, distance=" + moveDistance + "px");

        new Thread(() -> {
            Point lastMousePos = MouseInfo.getPointerInfo().getLocation();

            while (running.get()) {
                try {
                    Thread.sleep(idleCheckIntervalMillis);
                    Point currentPos = MouseInfo.getPointerInfo().getLocation();
                    if (currentPos.equals(lastMousePos)) {
                        moveMouse(currentPos);
                        logger.info("Mouse jiggled at: " + new Date());
                    }
                    lastMousePos = MouseInfo.getPointerInfo().getLocation();
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "KeepAwake-Thread").start();
    }

    public void stop() {
        running.set(false);
    }

    private void moveMouse(Point currentPos) {
        int x = currentPos.x;
        int y = currentPos.y;
        robot.mouseMove(x + moveDistance, y);
        robot.mouseMove(x, y);
    }
}
