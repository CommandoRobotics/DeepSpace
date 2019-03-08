package frc.apis;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

public class CargoIntake {

    //INTAKE MOTOR
    private Spark motor;

    //INTAKE HOLDING
    private Solenoid solenoid;
    private DigitalInput limitSwitch;

    private boolean releasing;
        private long releaseTime;

    //Minimum power at which the motors will turn
    private double minimumPower;

    private Communications communications;

    public CargoIntake(int motorPort, int solenoidPort, Communications communications) {
        this(new Spark(motorPort), new Solenoid(solenoidPort), communications);
    }

    public CargoIntake(Spark motor, Solenoid solenoid, Communications communications) {
        this.motor = motor;
        this.solenoid = solenoid;
        this.communications = communications;

        this.releasing = false;
            this.releaseTime = System.nanoTime();
        this.minimumPower = 0.05;
    }

    public void update() {
        if(releasing) {
            releasing = System.nanoTime() - releaseTime > 2E9;
            System.out.println("Release Percentage: " + ((System.nanoTime() - releaseTime) / 2E9));
        }
        if(communications.getDigitalPortInput(3) && !releasing) hold();
    }

    public double getMinimumPower() {
        return minimumPower;
    }

    public void setMinimumPower(double minimumPower) {
        this.minimumPower = Math.abs(minimumPower);
    }

    public void pullCargo(double power) {
        double truePower = (Math.abs(power) > minimumPower) ? power : minimumPower;
        motor.set(Math.abs(truePower));
    }

    public void pushCargo(double power) {
        double truePower = (Math.abs(power) > minimumPower) ? power : -minimumPower;
        motor.set(-Math.abs(truePower));
    }

    public void hold() {
        solenoid.set(false);
    }

    public void release() {
        solenoid.set(true);
        releasing = true;
        releaseTime = System.nanoTime();
    }

    public void stop() {
        motor.set(0);
    }
}