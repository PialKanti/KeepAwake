package org.example;

import picocli.CommandLine;

import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

@CommandLine.Command(
        name = "keepawake",
        mixinStandardHelpOptions = true,
        version = "KeepAwake 1.0",
        description = "Prevents system idle by moving the mouse periodically"
)
public class Application implements Runnable {
    @CommandLine.Option(names = {"-i", "--interval"}, description = "Idle check interval in milliseconds", defaultValue = "30000")
    private int interval;

    @CommandLine.Option(names = {"-d", "--distance"}, description = "Mouse move distance in pixels", defaultValue = "2")
    private int distance;

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    @Override
    public void run() {
        try {
            IdlePreventionService idlePreventionService = new IdlePreventionService(interval, distance);
            idlePreventionService.start();

            CountDownLatch latch = new CountDownLatch(1);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                idlePreventionService.stop();
                latch.countDown();
                logger.info("Shutting down KeepAwake...");
            }));

            latch.await();
        } catch (AWTException e) {
            logger.severe("Failed to initialize Robot: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }
}