package frc.apis;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchMechanism {

    private Solenoid solenoid;
    private boolean deployed;

    public HatchMechanism(int solenoidPort) {
        this(new Solenoid(solenoidPort));
    }

    public HatchMechanism(Solenoid solenoid) {
        this.solenoid = solenoid;
        retract();
    }

    public void deploy() {
        solenoid.set(true);
        deployed = true;
        SmartDashboard.putBoolean("Hatch Extended", deployed);
    }

    public void retract() {
        solenoid.set(false);
        deployed = false;
        SmartDashboard.putBoolean("Hatch Extended", deployed);
    }

    public void toggle() {
        if(!deployed) deploy();
        else retract();
    }

}