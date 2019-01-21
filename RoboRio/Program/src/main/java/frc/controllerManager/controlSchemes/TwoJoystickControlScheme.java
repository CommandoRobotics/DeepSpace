package frc.controllerManager.controlSchemes;

import frc.apis.MecanumChassis;
import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;

public class TwoJoystickControlScheme extends ControlScheme {

    private MecanumChassis chassis;

    private static final int JOYSTICK_ONE_PORT = 0;
    private static final int JOYSTICK_TWO_PORT = 1;

    public TwoJoystickControlScheme(MecanumChassis chassis) {
        super();
        addJoystick(JOYSTICK_ONE_PORT, new int[]{Y_AXIS}, new int[]{0, 1});
        addJoystick(JOYSTICK_TWO_PORT, new int[]{Y_AXIS}, new int[]{0, 1});

        this.chassis = chassis;
    }

    @Override
    public void controlRobot() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }

        chassis.driveMecanum(trackedJoysticks.get(1).getRawAxis(1), trackedJoysticks.get(0).getRawAxis(0), 0);
    }

}