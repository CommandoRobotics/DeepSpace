package frc.controllerManager.controlSchemes;

import frc.apis.MecanumChassis;
import frc.apis.CargoSystem;
import frc.apis.HatchMechanism;
import frc.controllerManager.ControlScheme;
import frc.controllerManager.TrackedJoystick;

public class TwoJoystickControlScheme extends ControlScheme {    

    private boolean hasChassis;
        private MecanumChassis chassis;
        private boolean driveControlsReversed;

    private boolean hasHatchMechanism;
        private HatchMechanism hatchMechanism;
    
    private boolean hasCargoSystem;
        private CargoSystem cargoSystem;

    private static final int JOYSTICK_ONE_PORT = 0;
    private static final int JOYSTICK_TWO_PORT = 1;

    private TrackedJoystick driverXbox;
    private TrackedJoystick mechanismXbox;

    private static double deadZone = 0.05;

    public TwoJoystickControlScheme(MecanumChassis chassis, HatchMechanism hatchMechanism,
        CargoSystem cargoSystem) {
        super();
        
        addJoystick(JOYSTICK_ONE_PORT, new int[]{}, new int[]{LOGITECH_TOP_BUTTON, LOGITECH_BOTTOM_BUTTON});
        addJoystick(JOYSTICK_TWO_PORT, new int[]{}, new int[]{});

        driverXbox = trackedJoysticks.get(0);
        mechanismXbox = trackedJoysticks.get(1);

        this.chassis = chassis;
        this.hasChassis = this.chassis != null;
        this.driveControlsReversed = true;

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
        if(driverXbox.buttonWasJustPressed(LOGITECH_TOP_BUTTON)) driveControlsReversed = !driveControlsReversed;

        double reverseFactor = driveControlsReversed ? -1 : 1;

        chassis.driveMecanum(
            driverXbox.getRawAxis(LOGITECH_X_AXIS_1) * reverseFactor,
            -driverXbox.getRawAxis(LOGITECH_Y_AXIS_1) * reverseFactor,
            driverXbox.getRawAxis(LOGITECH_X_AXIS_2));
    }

    private void controlHatchMechanism() {
        if(!hasHatchMechanism) return;
        // if(logitech.buttonWasJustPressed(LOGITECH_RIGHT_BUMPER)) hatchMechanism.toggle();
        if(mechanismXbox.getRawButton(LOGITECH_LEFT_BUMPER)) hatchMechanism.deploy();
        else hatchMechanism.retract();
    }

    private void controlCargoSystem() {
        if(!hasCargoSystem) return;

        double leftPower = mechanismXbox.getRawAxis(LOGITECH_LEFT_TRIGGER),
            rightPower = mechanismXbox.getRawAxis(LOGITECH_RIGHT_TRIGGER);

        if(mechanismXbox.getRawButton(LOGITECH_TOP_BUTTON)) {
            cargoSystem.expelAllContents(0.5);
        } else if(mechanismXbox.getRawButton(LOGITECH_BOTTOM_BUTTON)) {
            cargoSystem.deployIntake(1.0);
        } else if(mechanismXbox.getRawButton(LOGITECH_LEFT_BUTTON) || mechanismXbox.getRawButton(LOGITECH_RIGHT_BUTTON)) {
            cargoSystem.retractIntake(0.5);
        } else if(leftPower > deadZone) {
            cargoSystem.intake(cargoIntakeSpeedStep(leftPower), cargoConveyorBeltSpeedStep(leftPower));
        } else if(rightPower > deadZone) {
            cargoSystem.shoot(cargoOutputSpeedStep(rightPower));
        } else {
            cargoSystem.stop();
        }
        
    }
    
    private double cargoIntakeSpeedStep(double basePower) {
        return (basePower > 0.5) ? 0.3 : 0;
    }

    private double cargoConveyorBeltSpeedStep(double basePower) {
        return (basePower > 0.5) ? 0.6 : 0;
    }

    private double cargoOutputSpeedStep(double basePower) {
        return (basePower > 0.5) ? 0.8 : 0;
    }

    public boolean driverAssistRequested() {
		return driverXbox.buttonWasJustPressed(LOGITECH_BOTTOM_BUTTON);
	}

}