package frc.apis;

import edu.wpi.first.wpilibj.Solenoid;

public class HatchMechanism {

    private Solenoid solenoid1, solenoid2;

    public HatchMechanism(int solenoidPort1, int solenoidPort2) {
        this(new Solenoid(solenoidPort1), new Solenoid(solenoidPort2));
    }

    public HatchMechanism(Solenoid solenoid1, Solenoid solenoid2) {
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
    }

    public void deploy() {
        solenoid1.set(true);
        solenoid2.set(false);
    }

    public void retract() {
        solenoid1.set(true);
        solenoid2.set(false);
    }

}