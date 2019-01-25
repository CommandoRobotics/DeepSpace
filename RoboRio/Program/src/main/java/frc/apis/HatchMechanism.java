package frc.apis;

import edu.wpi.first.wpilibj.Solenoid;

public class HatchMechanism {

    private Solenoid solenoid;

    public HatchMechanism(int solenoidPort) {
        this(new Solenoid(solenoidPort));
    }

    public HatchMechanism(Solenoid solenoid) {
        this.solenoid = solenoid;
    }

    public void deploy() {
        solenoid.set(true);
    }

    public void retract() {
        solenoid.set(false);
    }

}