package frc.apis;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PDP extends PowerDistributionPanel {

    public PDP() {
        super();
    }

    public PDP(int canID) {
        super(canID);
    }

    @Override
    public double getTemperature() {
        return super.getTemperature() * 1.8 + 32;
    }

    public void updatePowerUsage() {
        SmartDashboard.putNumber("Front Left Drive Motor", super.getCurrent(0));
        SmartDashboard.putNumber("Rear Left Drive Motor", super.getCurrent(1));
        SmartDashboard.putNumber("Front Right Drive Motor", super.getCurrent(3));
        SmartDashboard.putNumber("Rear Right Drive Motor", super.getCurrent(2));
        SmartDashboard.putNumber("Rainbow Road", super.getCurrent(4));
        SmartDashboard.putNumber("Left Cargo Outake Motor", super.getCurrent(5));
        SmartDashboard.putNumber("Cargo Intake Motor", super.getCurrent(6));
        SmartDashboard.putNumber("Right Cargo Outake Motor", super.getCurrent(7));
        SmartDashboard.putNumber("Arm Winch Motor", super.getCurrent(8));
        SmartDashboard.putNumber("Total AMPs", super.getTotalCurrent());
        SmartDashboard.putNumber("Battery Power", super.getVoltage());
        SmartDashboard.putNumber("PDP Temperature", super.getTemperature() * 1.8 + 32);
        //Values are put twice so that I can use progress bar and a seperate number
        SmartDashboard.putNumber("0", super.getCurrent(0));
        SmartDashboard.putNumber("1", super.getCurrent(1));
        SmartDashboard.putNumber("3", super.getCurrent(3));
        SmartDashboard.putNumber("2", super.getCurrent(2));
        SmartDashboard.putNumber("4", super.getCurrent(4));
        SmartDashboard.putNumber("5", super.getCurrent(5));
        SmartDashboard.putNumber("6", super.getCurrent(6));
        SmartDashboard.putNumber("7", super.getCurrent(7));
        SmartDashboard.putNumber("8", super.getCurrent(8));
        SmartDashboard.putNumber("TA", super.getTotalCurrent());
        SmartDashboard.putNumber("BP", super.getVoltage());
        SmartDashboard.putNumber("PDP T", super.getTemperature() * 1.8 + 32);
    }

}