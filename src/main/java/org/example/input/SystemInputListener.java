package org.example.input;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.*;
import org.example.IdlePreventionService;

public class SystemInputListener implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener, NativeMouseWheelListener {
    private final IdlePreventionService idlePreventionService;

    public SystemInputListener(IdlePreventionService idlePreventionService) {
        this.idlePreventionService = idlePreventionService;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
        idlePreventionService.updateActivity();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        idlePreventionService.updateActivity();
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
        idlePreventionService.updateActivity();
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
        idlePreventionService.updateActivity();
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
        idlePreventionService.updateActivity();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeEvent) {
        idlePreventionService.updateActivity();
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeEvent) {
        idlePreventionService.updateActivity();
    }
}
