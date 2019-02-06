package frc.controllerManager;

import java.util.List;
import java.util.ArrayList;

public abstract class ControlScheme {

    //AXIS CONSTANTS
    protected static final int X_AXIS = 0;
    protected static final int Y_AXIS = 1;
    protected static final int Z_AXIS = 2;
    

    protected static final int LOGITECH_X_AXIS_1 = 0;
    protected static final int LOGITECH_Y_AXIS_1 = 1;
    protected static final int LOGITECH_LEFT_TRIGGER = 2;
    protected static final int LOGITECH_RIGHT_TRIGGER = 3;
    protected static final int LOGITECH_X_AXIS_2 = 4;
    protected static final int LOGITECH_Y_AXIS_2 = 5;

    protected static final int LOGITECH_TOP_BUTTON = 4;
    protected static final int LOGITECH_BOTTOM_BUTTON = 1;
    protected static final int LOGITECH_LEFT_BUTTON = 3;
    protected static final int LOGITECH_RIGHT_BUTTON = 2;
    protected static final int LOGITECH_LEFT_BUMPER = 7;
    protected static final int LOGITECH_RIGHT_BUMPER = 8;

    //JOYSTICKS
    //These joysticks are tracked for if they are receiving input and which input have just been activated (for toggling)
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

    //Extend this class and override this function to control the robot with the tracked joysticks
    public abstract void controlRobot();

}