package org.example.input;

import java.awt.*;

public class MouseSimulator {
    private final Robot robot;

    public MouseSimulator() throws AWTException {
        this.robot = new Robot();
    }

    public void jiggle(int offsetX) {
        Point currentPosition = getCurrentPosition();

        int x = currentPosition.x;
        int y = currentPosition.y;

        robot.mouseMove(x + offsetX, y);
        robot.mouseMove(x, y);
    }

    public Point getCurrentPosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
}
