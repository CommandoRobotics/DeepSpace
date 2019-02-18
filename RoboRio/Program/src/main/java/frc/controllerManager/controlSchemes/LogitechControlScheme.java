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
    private static double deadZone = 0.05;

    public LogitechControlScheme(MecanumChassis chassis, HatchMechanism hatchMechanism,
        CargoSystem cargoSystem) {
        super();
        addJoystick(JOYSTICK_ONE_PORT, new int[]{}, new int[]{LOGITECH_BOTTOM_BUTTON});

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
        // if(logitech.buttonWasJustPressed(LOGITECH_RIGHT_BUMPER)) hatchMechanism.toggle();
        if(logitech.getRawButton(LOGITECH_RIGHT_BUMPER)) hatchMechanism.deploy();
        else hatchMechanism.retract();
    }

    private void controlCargoSystem() {
        if(!hasCargoSystem) return;

        double leftPower = logitech.getRawAxis(LOGITECH_LEFT_TRIGGER),
            rightPower = logitech.getRawAxis(LOGITECH_RIGHT_TRIGGER);

        if(leftPower > deadZone && rightPower > deadZone) {
            double averagePower = (leftPower + rightPower) / 2;
            cargoSystem.expelAllContents(averagePower);
        } else if(leftPower > deadZone) {
            cargoSystem.intake(leftPower);
        } else if(rightPower > deadZone) {
            cargoSystem.shoot(rightPower);
        } else {
            cargoSystem.stop();
        }
        
	}
	
	public boolean driverAssistRequested() {
		return logitech.buttonWasJustPressed(LOGITECH_BOTTOM_BUTTON);
	}

}