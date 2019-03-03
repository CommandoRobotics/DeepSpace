package frc.apis;

import edu.wpi.first.wpilibj.Spark;

public class CargoIntake {

    //INTAKE MOTOR
    private Spark motor;

    //Minimum power at which the motors will turn
    private double minimumPower;

    public CargoIntake(int motorPort) {
        this(new Spark(motorPort));
    }

    public CargoIntake(Spark motor) {
        this.motor = motor;
        this.minimumPower = 0.05;
    }

    public void update() {
        
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

    public void stop() {
        motor.set(0);
    }
}