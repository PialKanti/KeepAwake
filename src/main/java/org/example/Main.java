package org.example;

import picocli.CommandLine;

import java.awt.*;

@CommandLine.Command(
        name = "keepawake",
        mixinStandardHelpOptions = true,
        version = "KeepAwake 1.0",
        description = "Prevents system idle by moving the mouse periodically"
)
public class Main implements Runnable {
    @CommandLine.Option(names = {"-i", "--interval"}, description = "Idle check interval in milliseconds", defaultValue = "30000")
    private int interval;

    @CommandLine.Option(names = {"-d", "--distance"}, description = "Mouse move distance in pixels", defaultValue = "1")
    private int distance;

    @Override
    public void run() {
        try {
            KeepAwake keepAwake = new KeepAwake(interval, distance);
            keepAwake.start();
        } catch (AWTException e) {
            System.err.println("Failed to initialize Robot: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}