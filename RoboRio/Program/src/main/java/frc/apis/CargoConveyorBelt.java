package frc.apis;

import edu.wpi.first.wpilibj.Spark;

public class CargoConveyorBelt {

    private Spark motor;

    //Minimum power at which the motors will turn
    private double minimumPower;

    public CargoConveyorBelt(int motorPort) {
        this(new Spark(motorPort));
    }

    public CargoConveyorBelt(Spark motor) {
        this.motor = motor;
        this.minimumPower = 0.05;
    }

    public void update() {

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