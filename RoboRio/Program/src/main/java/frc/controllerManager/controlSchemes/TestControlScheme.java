package frc.controllerManager.controlSchemes;

import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;

public class TestControlScheme extends ControlScheme {
    private static final int JOYSTICK_ONE_PORT = 0;
    private static final int JOYSTICK_TWO_PORT = 1;

    public TestControlScheme() {
        super();
        addJoystick(JOYSTICK_ONE_PORT, new int[]{X_AXIS}, new int[]{0, 1});
        addJoystick(JOYSTICK_TWO_PORT, new int[]{Y_AXIS}, new int[]{0, 1});
    }

    @Override
    public void controlRobot() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }

        System.out.println("Joystick 0 Axis 0: " + trackedJoysticks.get(0).getRawAxis(X_AXIS));
        System.out.println("Joystick 1 Axis 1: " + trackedJoysticks.get(1).getRawAxis(Y_AXIS));
        System.out.println();
    }

}