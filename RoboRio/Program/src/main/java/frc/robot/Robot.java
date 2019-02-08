/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.apis.*;
import frc.controllerManager.controlSchemes.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private int currentState;
  private static final int DRIVER_CONTROL_STATE = 0;
  private static final int DRIVER_ASSIST_STATE = 1;
  
  private UsbCamera camera;

  private MecanumChassis chassis;
  private HatchMechanism hatchMechanism;
  private CargoSystem cargoSystem;

  private LogitechControlScheme controlScheme;
  private DriverAssistControlScheme driverAssist;

  private Communications communications;
  private static fianl int WHICH_ALLIANCE_DIGITAL_PORT = 5;

  private boolean onBlueAlliance;
  private boolean background;

  @Override
  public void robotInit() {
    this.currentState = DRIVER_CONTROL_STATE;
    camera = CameraServer.getInstance().startAutomaticCapture(0);

    onBlueAlliance = false;
	  
    background = true;
    SmartDashboard.putBoolean(" ", background);

    this.chassis = new MecanumChassis(new Spark(3), new Spark(1), new Spark(0), new Spark(2));
    this.hatchMechanism = new HatchMechanism(0, 1);
    this.cargoSystem = new CargoSystem(new CargoIntake(4, 0), new CargoConveyorBelt(7), new CargoOutput(5, 6));

	  this.communications = new Communications(new int[]{0, 1}, new int[]{}, new int[]{1, 2}, new int[]{3, WHICH_ALLIANCE_DIGITAL_PORT});
	  
    this.controlScheme = new LogitechControlScheme(chassis, hatchMechanism, cargoSystem);//9,8,7,6
	this.driverAssist = new DriverAssistControlScheme(communications, chassis);
  }

  @Override
  public void robotPeriodic() {
	onBlueAlliance = (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.kBlue);
	communications.sendDigitalPortOutput(WHICH_ALLIANCE_DIGITAL_PORT, onBlueAlliance);
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {
    SmartDashboard.putBoolean(" ", driverAssist.canBegin());
    
    switch(currentState) {
       case DRIVER_CONTROL_STATE:
		 if(controlScheme.driverAssistRequested()) {
			driverAssist.start();
			currentState = DRIVER_ASSIST_STATE;
		 } else {
         	controlScheme.controlRobot();
		 }
         break;
       case DRIVER_ASSIST_STATE:   
		 if(driverAssist.isFinished() || controlScheme.driverAssistRequested()) {
		 	currentState = DRIVER_CONTROL_STATE;
		 } else {
			driverAssist.controlRobot();
		 }
         break;
    }
  }

  @Override
  public void testPeriodic() {
  }
}
