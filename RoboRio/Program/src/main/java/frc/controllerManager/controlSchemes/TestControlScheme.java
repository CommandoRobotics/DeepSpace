package frc.controllerManager.controlSchemes;

import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;

import edu.wpi.first.wpilibj.Spark;

public class TestControlScheme extends ControlScheme {
    private static final int JOYSTICK_ONE_PORT = 0;
    private static final int JOYSTICK_TWO_PORT = 1;

    Spark leftFront, rightFront, leftRear, rightRear;

    public TestControlScheme(int lfPort, int rfPort, int lrPort, int rrPort) {
        super();
        addJoystick(JOYSTICK_ONE_PORT, new int[]{X_AXIS}, new int[]{1, 2});
        addJoystick(JOYSTICK_TWO_PORT, new int[]{Y_AXIS}, new int[]{1, 2});

        leftFront = new Spark(lfPort);
        rightFront = new Spark(rfPort);
        leftRear = new Spark(lrPort);
        rightRear = new Spark(rrPort);
    }

    @Override
    public void controlRobot() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }

        double leftY = trackedJoysticks.get(0).getRawAxis(Y_AXIS);
        double rightY = trackedJoysticks.get(1).getRawAxis(Y_AXIS);

        leftFront.set(-leftY); leftRear.set(-leftY);
        rightFront.set(rightY); rightRear.set(rightY);
    }

}