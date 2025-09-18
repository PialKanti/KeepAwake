package org.example.input;

import java.awt.*;

public class MouseSimulator {
    private final Robot robot;

    public MouseSimulator() throws AWTException {
        this.robot = new Robot();
    }

    public void move(int x, int y) {
        robot.mouseMove(x, y);
    }

    public Point getCurrentPosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
}
