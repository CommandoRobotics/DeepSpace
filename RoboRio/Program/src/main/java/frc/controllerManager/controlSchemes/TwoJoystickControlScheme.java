package frc.controllerManager.controlSchemes;

import frc.apis.MecanumChassis;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private static final int WINCH_RETRACT_BUTTON = -1;
    private static final int EXPEL_ALL_CONTENTS_BUTTON = -1;

    public TwoJoystickControlScheme(MecanumChassis chassis, HatchMechanism hatchMechanism,
        CargoSystem cargoSystem) {
        super();
        
        addJoystick(JOYSTICK_ONE_PORT, new int[]{}, new int[]{LOGITECH_TOP_BUTTON, LOGITECH_BOTTOM_BUTTON});
        addJoystick(JOYSTICK_TWO_PORT, new int[]{}, new int[]{});

        driverXbox = trackedJoysticks.get(0);
        mechanismXbox = trackedJoysticks.get(1);

        this.chassis = chassis;
        this.hasChassis = this.chassis != null;
        this.driveControlsReversed = false;

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
        if(driverXbox.buttonWasJustPressed(LOGITECH_TOP_BUTTON)) {
            driveControlsReversed = !driveControlsReversed;
            SmartDashboard.putString("Drive Reversed", (driveControlsReversed) ? "Cargo Is Forward" : "Hatch Is Forward");
        }

        double reverseFactor = driveControlsReversed ? -1 : 1;

        chassis.driveMecanum(
            driverXbox.getRawAxis(LOGITECH_X_AXIS_1) * reverseFactor,
            -driverXbox.getRawAxis(LOGITECH_Y_AXIS_1) * reverseFactor,
            driverXbox.getRawAxis(LOGITECH_X_AXIS_2));
    }

    private void controlHatchMechanism() {
        if(!hasHatchMechanism) return;

        if(mechanismXbox.getPOV() == 180) cargoSystem.releaseIntakeArm();
        else if(mechanismXbox.getPOV() == 0) cargoSystem.holdIntakeArm();

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
            cargoSystem.deployIntake(0.5);
        } else if(mechanismXbox.getRawButton(LOGITECH_LEFT_BUTTON) || mechanismXbox.getRawButton(LOGITECH_RIGHT_BUTTON)) {
            cargoSystem.retractIntake(1.0);
        } else if(leftPower > deadZone) {
            cargoSystem.intake(cargoIntakeSpeedStep(leftPower), 1.0);
        } else if(rightPower > deadZone) {
            cargoSystem.shoot(cargoOutputSpeedStep(rightPower));
        } else if(Math.abs(mechanismXbox.getRawAxis(LOGITECH_Y_AXIS_1)) > deadZone) {
            cargoSystem.setCargoOutput(Math.abs(mechanismXbox.getRawAxis(LOGITECH_Y_AXIS_1)));
        } else if(Math.abs(mechanismXbox.getRawAxis(LOGITECH_Y_AXIS_2)) > deadZone) {
            cargoSystem.setConveyorBelt(-mechanismXbox.getRawAxis(LOGITECH_Y_AXIS_2));
        } else {
            cargoSystem.stop();
        }
        
    }
    
    private double cargoIntakeSpeedStep(double basePower) {
        return (basePower > 0.5) ? 0.5 : 0;
    }

    private double cargoConveyorBeltSpeedStep(double basePower) {
        return (basePower > 0.5) ? 0.8 : 0;
    }

    private double cargoOutputSpeedStep(double basePower) {
        return (basePower > 0.5) ? 0.55 : 0;
    }

    public boolean driverAssistRequested() {
        return driverXbox.buttonWasJustPressed(LOGITECH_BOTTOM_BUTTON);
	}

}