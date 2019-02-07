package frc.apis;

import edu.wpi.first.wpilibj.Solenoid;

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
        solenoid1.set(false);
        solenoid2.set(true);
        deployed = true;
    }

    public void retract() {
        solenoid1.set(true);
        solenoid2.set(false);
        deployed = false;
    }

    public void toggle() {
        if(!deployed) deploy();
        else retract();
    }

}