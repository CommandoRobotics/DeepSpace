/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.apis.MecanumChassis;
import frc.controllerManager.controlSchemes.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private int currentState;
  private static final int DRIVER_CONTROL_STATE = 0;
  private static final int COLOR_ALIGN_STATE = 1;

  private UsbCamera camera;

  private MecanumChassis chassis;

  private TwoJoystickControlScheme controlScheme;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    this.currentState = DRIVER_CONTROL_STATE;

    camera = CameraServer.getInstance().startAutomaticCapture(0);

    this.chassis = new MecanumChassis(new Spark(3), new Spark(1), new Spark(0), new Spark(2));

    this.controlScheme = new TwoJoystickControlScheme(chassis);//9,8,7,6
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  @Override
  public void teleopPeriodic() {
    switch(currentState) {
       case DRIVER_CONTROL_STATE:
         controlScheme.controlRobot();
         break;
       case COLOR_ALIGN_STATE:   
         break;
    }
  }

  @Override
  public void testPeriodic() {
  }
}
