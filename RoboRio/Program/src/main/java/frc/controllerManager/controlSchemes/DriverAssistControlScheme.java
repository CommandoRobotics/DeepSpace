package frc.controllerManager.controlSchemes;
	
import frc.controllerManager.ControlScheme;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.apis.Communications;
import frc.apis.MecanumChassis;
import frc.apis.SerialData;

public class DriverAssistControlScheme extends ControlScheme {

	private boolean finished;

	private Communications communications;

	private static final int SERIAL_PORT = 0;

	private MecanumChassis chassis;

	public DriverAssistControlScheme(Communications communications, MecanumChassis chassis) {
		this.communications = communications;

		this.chassis = chassis;
		
		this.finished = false;
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

			SmartDashboard.putNumber("DriverAssist Strafe Power", strafePower);
			SmartDashboard.putNumber("DriverAssist Drive Power", drivePower);
			SmartDashboard.putNumber("DriverAssist Rotate Power", rotatePower);

			System.out.println("Driving at " + drivePower + " " + strafePower + " " + rotatePower);
			chassis.driveMecanum(strafePower, drivePower, rotatePower);
		} else {
			chassis.stop();
			System.out.println("Requested data has expired.");
			//finished = true;
		}

		System.out.println("===End of Driver Assist Frame===");
	}

	public boolean isFinished() {
		return finished;
	}
}