package frc.controllerManager.controlSchemes;

import frc.apis.MecanumChassis;
import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;
import edu.wpi.first.wpilibj.Joystick;
public class TwoJoystickControlScheme extends ControlScheme {

    private MecanumChassis chassis;
    private TrackedJoystick translationJoystick, rotationJoystick;
    private Joystick leftJoy;
    private Joystick rightJoy;
    private static final int JOYSTICK_ONE_PORT = 0;
    private static final int JOYSTICK_TWO_PORT = 1;

    public TwoJoystickControlScheme(MecanumChassis chassis) {
        super();
        addJoystick(JOYSTICK_ONE_PORT, new int[]{X_AXIS, Y_AXIS}, new int[]{1, 2});
        addJoystick(JOYSTICK_TWO_PORT, new int[]{X_AXIS}, new int[]{1, 2});

        translationJoystick = trackedJoysticks.get(0);
        rotationJoystick = trackedJoysticks.get(1);
        this.chassis = chassis;
    }

    @Override
    public void controlRobot() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }
        
        chassis.driveMecanum(translationJoystick.getRawAxis(X_AXIS), -translationJoystick.getRawAxis(Y_AXIS), rotationJoystick.getRawAxis(X_AXIS));
    }

}