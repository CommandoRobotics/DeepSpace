package frc.apis;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchMechanism {

    private Solenoid solenoid1, solenoid2;
    private boolean deployed;

    public HatchMechanism(int solenoidPort1, int solenoidPort2) {
        this(new Solenoid(solenoidPort1), new Solenoid(solenoidPort2));
    }

    public HatchMechanism(Solenoid solenoid1, Solenoid solenoid2) {
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
        retract();
    }

    public void deploy() {
        solenoid1.set(true);
        deployed = true;
        SmartDashboard.putBoolean("Hatch Extended", deployed);
    }

    public void retract() {
        solenoid1.set(false);
        deployed = false;
        SmartDashboard.putBoolean("Hatch retractex", deployed);
    }

    public void toggle() {
        if(!deployed) deploy();
        else retract();
    }

}