package frc.controllerManager.controlSchemes;
	
import frc.controllerManager.ControlScheme;
import frc.apis.Communications;
import frc.apis.MecanumChassis;
import frc.apis.SerialData;

public class DriverAssistControlScheme extends ControlScheme {

	private static final double ANALOG_MEDIAN_VOLTAGE = 2.5;
	private static final double ANALOG_DEADZONE = 0.25;
	private static final double MINIMUM_ANALOG = 0.05;
	
	private boolean finished;
	private boolean canShoot;

	private Communications communications;

	//TODO: Set all of these ports to what they should be
	private static final int DRIVER_ASSIST_CAN_BEGIN_PORT = 1;
	private static final int DRIVER_ASSIST_FINISHED_DIGITAL_PORT = 2;
	private static final int CAN_SHOOT_DIGITAL_PORT = 3;
	private static final int SERIAL_PORT = 0;

	private MecanumChassis chassis;

	public DriverAssistControlScheme(Communications communications, MecanumChassis chassis) {
		this.communications = communications;

		this.chassis = chassis;
		
		this.finished = false;
		this.canShoot = false;
	}

	public void start() {
		System.out.println("Starting Driver Assist");
		//If we are being told that the driver assist program cannot begin, then we want to immediately declare the program to be finished running.
		finished = false;
		System.out.println("Can we start? " + !finished);
	}

	public void controlRobot() {
		//If we are done, then do NOT check the input ports for controls
		if(finished) {
			chassis.stop();
			return;
		}

		SerialData serialData = communications.getSerialData(SERIAL_PORT);
		if(serialData.dataGood()) {
			double strafePower = SerialData.parsePercentage(serialData, 'x');
			double drivePower = SerialData.parsePercentage(serialData, 'z');
			double rotatePower = SerialData.parsePercentage(serialData, 'r');

			System.out.println("Driving at " + drivePower + " " + strafePower + " " + rotatePower);
			chassis.driveMecanum(strafePower, drivePower, rotatePower);
		} else {
			chassis.stop();
			System.out.println("Requested data has expired.");
			//finished = true;
		}

		/*
		chassis.driveMecanum(
			scaleAnalogInput(communications.getAnalogPortInput(DRIVE_FORWARD_ANALOG_PORT)),
			scaleAnalogInput(communications.getAnalogPortInput(STRAFE_ANALOG_PORT)),
			scaleAnalogInput(communications.getAnalogPortInput(ROTATE_ANALOG_PORT)));

		System.out.println("Finished port input" + communications.getDigitalPortInput(DRIVER_ASSIST_FINISHED_DIGITAL_PORT));
		if(communications.getDigitalPortInput(DRIVER_ASSIST_FINISHED_DIGITAL_PORT)) {
			finished = true;
			canShoot = communications.getDigitalPortInput(CAN_SHOOT_DIGITAL_PORT);
		}
		*/

		System.out.println("===End of Driver Assist Frame===");
	}

	public boolean isFinished() {
		return finished;
	}
}