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
import edu.wpi.first.wpilibj.DriverStation;

@SuppressWarnings("unused")
public class Robot extends TimedRobot {
  private int currentState;
  private static final int DRIVER_CONTROL_STATE = 0;
  private static final int DRIVER_ASSIST_STATE = 1;
  
  private UsbCamera camera;

  private MecanumChassis chassis;
  private HatchMechanism hatchMechanism;
  private CargoSystem cargoSystem;
  private PDP pdp;

  private TwoJoystickControlScheme controlScheme;
  private DriverAssistControlScheme driverAssist;

  private Communications communications;
  private static final int WHICH_ALLIANCE_DIGITAL_PORT = 2;

  private boolean onBlueAlliance;
  private boolean background;

  @Override
  public void robotInit() {
    this.currentState = DRIVER_CONTROL_STATE;
    camera = CameraServer.getInstance().startAutomaticCapture(0);

    onBlueAlliance = false;
	  
    background = true;
    SmartDashboard.putBoolean(" ", background);

    this.communications = new Communications(
      new int[]{0, 1},
      new int[]{},
      new int[]{0, 1, 2},
      new int[]{3, WHICH_ALLIANCE_DIGITAL_PORT},
      new int[]{0});

    this.chassis = new MecanumChassis(new Spark(0), new Spark(1), new Spark(3), new Spark(2));
    this.hatchMechanism = new HatchMechanism(0, 1);
    this.cargoSystem = new CargoSystem(new CargoIntake(6), new CargoConveyorBelt(4), new CargoOutput(5, 7), new ArmWinch(8), communications);
    this.pdp = new PDP();
    
    this.controlScheme = new TwoJoystickControlScheme(chassis, hatchMechanism, cargoSystem);
	  this.driverAssist = new DriverAssistControlScheme(communications, chassis);
  }

  @Override
  public void robotPeriodic() {
    onBlueAlliance = (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue);
    communications.sendDigitalPortOutput(WHICH_ALLIANCE_DIGITAL_PORT, onBlueAlliance);
  }

  @Override
  public void autonomousInit() {
    initializeRobot();
  }

  @Override
  public void autonomousPeriodic() {
    updateRobot();
  }

  @Override
  public void teleopInit() {
    initializeRobot();
  }

  @Override
  public void teleopPeriodic() {
    updateRobot();
  }

  public void initializeRobot() {
    this.currentState = DRIVER_CONTROL_STATE;
    communications.reset();
  }

  public void updateRobot() {
    pdp.updatePowerUsage();

    communications.update();

    controlScheme.update();

    switch(currentState) {
      case DRIVER_CONTROL_STATE:
        SmartDashboard.putBoolean("Driver Assist Running", false);
        if(controlScheme.driverAssistRequested()) {
          System.out.println("Driver Assist Requested");
          driverAssist.start();
          currentState = DRIVER_ASSIST_STATE;
        } else {
          controlScheme.controlRobot();
        }
        break;
      case DRIVER_ASSIST_STATE:
        SmartDashboard.putBoolean("Driver Assist Running", true);
        if(driverAssist.isFinished() || controlScheme.driverAssistRequested()) {
          System.out.println("Driver Assist Stopped");
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
