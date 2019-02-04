package frc.controllerManager.controlSchemes;

import frc.apis.MecanumChassis;
import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;

public class OneJoystickControlScheme extends ControlScheme {

    private MecanumChassis chassis;
    private TrackedJoystick joystick;

    private static final int JOYSTICK_ONE_PORT = 0;

    public OneJoystickControlScheme(MecanumChassis chassis) {
        super();
        addJoystick(JOYSTICK_ONE_PORT, new int[]{X_AXIS, Y_AXIS, Z_AXIS}, new int[]{1, 2});

        joystick = trackedJoysticks.get(0);

        this.chassis = chassis;
    }

    @Override
    public void controlRobot() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }

        chassis.driveMecanum(joystick.getRawAxis(X_AXIS), -joystick.getRawAxis(Y_AXIS), joystick.getRawAxis(Z_AXIS));
    }

}