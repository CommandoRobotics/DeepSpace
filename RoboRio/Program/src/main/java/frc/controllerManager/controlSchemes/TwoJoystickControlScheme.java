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

    //DRIVER CONTROLS
    private static final int STRAFE_AXIS = LOGITECH_X_AXIS_1;
    private static final int DRIVE_AXIS = LOGITECH_Y_AXIS_1;
    private static final int ROTATE_AXIS = LOGITECH_X_AXIS_2;

    private static final int DRIVER_ASSIST_BUTTON = LOGITECH_TOP_BUTTON;

    //MECHANISM CONTROLS
    private static final int HATCH_DEPLOY_BUTTON = LOGITECH_LEFT_BUMPER;

    private static final int CARGO_INTAKE_BUTTON = LOGITECH_LEFT_TRIGGER;
    private static final int CARGO_SHOOT_BUTTON = LOGITECH_RIGHT_TRIGGER;
    private static final int CARGO_INTAKE_DEPLOY_BUTTON = LOGITECH_BOTTOM_BUTTON;
    private static final int CARGO_INTAKE_RETRACT_BUTTON_1 = LOGITECH_LEFT_BUTTON;
    private static final int CARGO_INTAKE_RETRACT_BUTTON_2 = LOGITECH_RIGHT_BUTTON;
    private static final int CARGO_EXPEL_BUTTON = LOGITECH_TOP_BUTTON;
    private static final int CARGO_CONVEYOR_BELT_AXIS = LOGITECH_Y_AXIS_2;
    private static final int CARGO_SHOOTER_AXIS = LOGITECH_Y_AXIS_1;

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
        if(driverXbox.buttonWasJustPressed(DRIVER_ASSIST_BUTTON)) {
            driveControlsReversed = !driveControlsReversed;
            SmartDashboard.putString("Drive Reversed", (driveControlsReversed) ? "Cargo Is Forward" : "Hatch Is Forward");
        }

        double reverseFactor = driveControlsReversed ? -1 : 1;

        chassis.driveMecanum(
            driverXbox.getRawAxis(STRAFE_AXIS) * reverseFactor,
            -driverXbox.getRawAxis(DRIVE_AXIS) * reverseFactor,
            driverXbox.getRawAxis(ROTATE_AXIS));
    }

    private void controlHatchMechanism() {
        if(!hasHatchMechanism) return;

        if(mechanismXbox.getPOV() == 180) cargoSystem.releaseIntakeArm();
        else if(mechanismXbox.getPOV() == 0) cargoSystem.holdIntakeArm();

        if(mechanismXbox.getRawButton(HATCH_DEPLOY_BUTTON)) hatchMechanism.deploy();
        else hatchMechanism.retract();
    }

    private void controlCargoSystem() {
        if(!hasCargoSystem) return;

        double leftPower = mechanismXbox.getRawAxis(CARGO_INTAKE_BUTTON),
            rightPower = mechanismXbox.getRawAxis(CARGO_SHOOT_BUTTON);

        if(mechanismXbox.getRawButton(CARGO_EXPEL_BUTTON)) {
            cargoSystem.expelAllContents(0.5);
        } else if(mechanismXbox.getRawButton(CARGO_INTAKE_DEPLOY_BUTTON)) {
            cargoSystem.deployIntake(0.5);
        } else if(mechanismXbox.getRawButton(CARGO_INTAKE_RETRACT_BUTTON_1) || mechanismXbox.getRawButton(CARGO_INTAKE_RETRACT_BUTTON_2)) {
            cargoSystem.retractIntake(1.0);
        } else if(leftPower > deadZone) {
            cargoSystem.intake(cargoIntakeSpeedStep(leftPower), 1.0);
        } else if(rightPower > deadZone) {
            cargoSystem.shoot(cargoOutputSpeedStep(rightPower));
        } else if(Math.abs(mechanismXbox.getRawAxis(CARGO_SHOOTER_AXIS)) > deadZone) {
            cargoSystem.setCargoOutput(Math.abs(mechanismXbox.getRawAxis(CARGO_SHOOTER_AXIS)));
        } else if(Math.abs(mechanismXbox.getRawAxis(CARGO_CONVEYOR_BELT_AXIS)) > deadZone) {
            cargoSystem.setConveyorBelt(-mechanismXbox.getRawAxis(CARGO_CONVEYOR_BELT_AXIS));
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