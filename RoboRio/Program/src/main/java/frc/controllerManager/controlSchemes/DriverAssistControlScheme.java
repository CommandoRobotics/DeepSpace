package frc.controllerManager.controlSchemes;
	
import frc.controllerManager.ControlScheme;
import frc.apis.Communications;
import frc.apis.MecanumChassis;

public class DriverAssistControlScheme extends ControlScheme {

	private static final double ANALOG_MEDIAN_VOLTAGE = 2.5;
	private static final double ANALOG_DEADZONE = 0.25;
	private static final double MINIMUM_ANALOG = 0.05;
	
	private boolean finished;
	private boolean canShoot;

	private Communications communications;

	//TODO: Set all of these ports to what they should be
	private static final int STRAFE_ANALOG_PORT = 1;
	private static final int DRIVE_FORWARD_ANALOG_PORT = 0;
	private static final int ROTATE_ANALOG_PORT = 2;
	private static final int DRIVER_ASSIST_CAN_BEGIN_PORT = 1;
	private static final int DRIVER_ASSIST_FINISHED_DIGITAL_PORT = 2;
	private static final int CAN_SHOOT_DIGITAL_PORT = 3;

	private boolean hasChassis = false;
		private MecanumChassis chassis;

	public DriverAssistControlScheme(Communications communications, MecanumChassis chassis) {
		this.communications = communications;

		this.chassis = chassis;
		this.hasChassis = this.chassis != null;
		
		this.finished = false;
		this.canShoot = false;
	}

	public void start() {
		System.out.println("Starting Driver Assist");
		//If we are being told that the driver assist program cannot begin, then we want to immediately declare the program to be finished running.
		finished = !canBegin();
		canShoot = false;
		System.out.println("Can we start? " + !finished);
	}

	public void controlRobot() {
		//If we are done, then do NOT check the input ports for controls
		if(finished) {
			chassis.stop();
			return;
		}
		
		/*
		chassis.driveMecanum(
			scaleAnalogInput(communications.getAnalogPortInput(DRIVE_FORWARD_ANALOG_PORT)),
			scaleAnalogInput(communications.getAnalogPortInput(STRAFE_ANALOG_PORT)),
			scaleAnalogInput(communications.getAnalogPortInput(ROTATE_ANALOG_PORT)));
			*/
		
		// chassis.driveMecanum(strafePower, drivePower, rotationPower);

		System.out.println("Finished port input" + communications.getDigitalPortInput(DRIVER_ASSIST_FINISHED_DIGITAL_PORT));
		if(communications.getDigitalPortInput(DRIVER_ASSIST_FINISHED_DIGITAL_PORT)) {
			finished = true;
			canShoot = communications.getDigitalPortInput(CAN_SHOOT_DIGITAL_PORT);
		}
	}

	private double scaleAnalogInput(double input) {
		//Prevents negative values from being passed as parameters.
		//Also catches the potential edge case that would cause an absence of a signal to send the robot shooting off in one direction.
		 if(input < MINIMUM_ANALOG) return 0;
		if(Math.abs(input - ANALOG_MEDIAN_VOLTAGE) < ANALOG_DEADZONE) return 0;

		return (input - ANALOG_MEDIAN_VOLTAGE) / ANALOG_MEDIAN_VOLTAGE;
	}

	public boolean canBegin() {
		return communications.getDigitalPortInput(DRIVER_ASSIST_CAN_BEGIN_PORT);
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean getCanShoot() {
		return canShoot;
	}
}