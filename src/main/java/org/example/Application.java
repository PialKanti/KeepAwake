package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import org.example.input.SystemInputListener;
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

            SystemInputListener inputListener = new SystemInputListener(idlePreventionService);
            registerSystemInputListener(inputListener);

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

    private static void registerSystemInputListener(SystemInputListener inputListener) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            logger.severe("There was a problem registering the native hook.");
            logger.severe(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(inputListener);
        GlobalScreen.addNativeMouseListener(inputListener);
        GlobalScreen.addNativeMouseMotionListener(inputListener);
        GlobalScreen.addNativeMouseWheelListener(inputListener);
    }
}