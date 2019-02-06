package frc.controllerManager.controlSchemes;

import frc.apis.MecanumChassis;
import frc.apis.CargoSystem;
import frc.apis.HatchMechanism;
import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;

public class LogitechControlScheme extends ControlScheme {

    private boolean hasChassis;
        private MecanumChassis chassis;

    private boolean hasHatchMechanism;
        private HatchMechanism hatchMechanism;
    
    private boolean hasCargoSystem;
        private CargoSystem cargoSystem;

    private TrackedJoystick logitech;

    private static final int JOYSTICK_ONE_PORT = 0;

    public LogitechControlScheme(MecanumChassis chassis, HatchMechanism hatchMechanism,
        CargoSystem cargoSystem) {
        super();
        addJoystick(JOYSTICK_ONE_PORT,
            new int[]{LOGITECH_X_AXIS_1, LOGITECH_Y_AXIS_1,
                    LOGITECH_X_AXIS_2, LOGITECH_LEFT_TRIGGER, LOGITECH_RIGHT_TRIGGER},
            new int[]{LOGITECH_TOP_BUTTON, LOGITECH_BOTTOM_BUTTON});

        logitech = trackedJoysticks.get(0);

        this.chassis = chassis;
        this.hasChassis = this.chassis != null;

        this.hatchMechanism = hatchMechanism;
        this.hasHatchMechanism = this.hatchMechanism != null;

        this.cargoSystem = cargoSystem;
        this.hasCargoSystem = this.cargoSystem != null;
    }

    @Override
    public void controlRobot() {
        for(TrackedJoystick joystick : trackedJoysticks) {
            joystick.update();
        }

        controlChassis();
        controlHatchMechanism();
        controlCargoSystem();
    }

    private void controlChassis() {
        if(!hasChassis) return;
        chassis.driveMecanum(logitech.getRawAxis(LOGITECH_X_AXIS_1), -logitech.getRawAxis(LOGITECH_Y_AXIS_1), logitech.getRawAxis(LOGITECH_X_AXIS_2));
    }

    private void controlHatchMechanism() {
        if(!hasHatchMechanism) return;
        if(logitech.getRawButton(LOGITECH_RIGHT_BUMPER)) hatchMechanism.deploy();
        else hatchMechanism.retract();
    }

    private void controlCargoSystem() {
        if(!hasCargoSystem) return;

        double forwardPower = logitech.getRawAxis(LOGITECH_LEFT_TRIGGER),
            backwardPower = logitech.getRawAxis(LOGITECH_RIGHT_TRIGGER);

        double truePower = (Math.abs(forwardPower) > Math.abs(backwardPower)) ? forwardPower : -backwardPower;

        cargoSystem.setIntake(truePower);
        cargoSystem.setConveyorBelt(truePower);
        cargoSystem.setCargoOutput(-truePower);
    }

}