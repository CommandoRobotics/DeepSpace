package frc.controllerManager.controlSchemes;

import frc.apis.MecanumChassis;
import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;

public class LogitechControlScheme extends ControlScheme {

    private MecanumChassis chassis;
    private TrackedJoystick logitech;

    private static final int JOYSTICK_ONE_PORT = 0;

    public LogitechControlScheme(MecanumChassis chassis) {
        super();
        addJoystick(JOYSTICK_ONE_PORT, new int[]{LOGITECH_X_AXIS_1, LOGITECH_Y_AXIS_1,
            LOGITECH_X_AXIS_2}, new int[]{});

        logitech = trackedJoysticks.get(0);

        this.chassis = chassis;
    }

    @Override
    public void controlRobot() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }

        chassis.driveMecanum(logitech.getRawAxis(LOGITECH_X_AXIS_1), -logitech.getRawAxis(LOGITECH_Y_AXIS_1), logitech.getRawAxis(LOGITECH_X_AXIS_2));
    }

}