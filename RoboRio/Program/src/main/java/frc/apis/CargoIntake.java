package frc.apis;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;

public class CargoIntake {

    //INTAKE MOTORS
    private Spark motor;

    //LIMIT SWITCH--If this triggeres, then we have a cargo in the robot
    private DigitalInput limitSwitch;

    //Minimum power at which the motors will turn
    private double minimumPower;

    //TODO: Change this to an absolute number of motor ports
    public CargoIntake(int motorPort, int limitSwitchPort) {
        this(new Spark(motorPort), new DigitalInput(limitSwitchPort));
    }

    public CargoIntake(Spark motor, DigitalInput limitSwitch) {
        this.motor = motor;
        this.limitSwitch = limitSwitch;
        this.minimumPower = 0.05;
    }

    public void update() {
        if(limitSwitch.get()) {
            //If the limit switch is active, we have cargo loaded. We can stop spinning the wheels.
            stop();
        } 
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