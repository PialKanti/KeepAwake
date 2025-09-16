package org.example;

import picocli.CommandLine;

import java.awt.*;
import java.util.concurrent.CountDownLatch;

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

            CountDownLatch latch = new CountDownLatch(1);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                keepAwake.stop();
                latch.countDown();
                System.out.println("Shutting down KeepAwake...");
            }));

            latch.await();
        } catch (AWTException e) {
            System.err.println("Failed to initialize Robot: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}