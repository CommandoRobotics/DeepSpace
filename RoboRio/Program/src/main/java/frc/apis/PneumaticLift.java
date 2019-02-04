package frc.apis;

import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticLift {

    private Solenoid solenoid;
    
    public PneumaticLift(int solenoidPort) {
        this(new Solenoid(solenoidPort));
    }

    public PneumaticLift(Solenoid solenoid) {
        this.solenoid = solenoid;
    }

    public void raiseLift(double power) {
        solenoid.set(true);
    }

    public void lowerLift(double power) {
        solenoid.set(false);
    }

}