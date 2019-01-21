package frc.controllerManager;

import java.util.List;
import java.util.ArrayList;

public abstract class ControlScheme {

    protected static final int X_AXIS = 0;
    protected static final int Y_AXIS = 1;

    protected List<TrackedJoystick> trackedJoysticks;

    public ControlScheme() {
        this.trackedJoysticks = new ArrayList<>();
    }

    public void addJoystick(int port, int[] axesToTrack, int[] buttonsToTrack) {
        TrackedJoystick trackedJoystick = new TrackedJoystick(port);
        trackedJoystick.trackAxes(axesToTrack);
        trackedJoystick.trackButtons(buttonsToTrack);
        this.trackedJoysticks.add(trackedJoystick);
    }

    public void addJoystick(TrackedJoystick joystickToTrack) {
        trackedJoysticks.add(joystickToTrack);
    }

    public abstract void controlRobot();

}