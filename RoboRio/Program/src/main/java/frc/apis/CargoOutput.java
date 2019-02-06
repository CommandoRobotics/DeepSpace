package frc.apis;

import edu.wpi.first.wpilibj.Spark;

//Proper Name: GoGoCargo
public class CargoOutput {

    private Spark leftSpark, rightSpark;

    //Minimum power at which the motors will turn
    private double minimumPower;

    public CargoOutput(int leftSparkPort, int rightSparkPort) {
        this(new Spark(leftSparkPort), new Spark(rightSparkPort));
    }

    public CargoOutput(Spark leftSpark, Spark rightSpark) {
        this.leftSpark = leftSpark;
        this.rightSpark = rightSpark;
        this.minimumPower = 0.05;
    }

    public void update() {

    }

    public void pullCargo(double power) {
        double truePower = (Math.abs(power) > minimumPower) ? power : minimumPower;
        leftSpark.set(Math.abs(truePower));
        rightSpark.set(Math.abs(truePower));
    }

    public void pushCargo(double power) {
        double truePower = (Math.abs(power) > minimumPower) ? power : -minimumPower;
        leftSpark.set(-Math.abs(truePower));
        rightSpark.set(-Math.abs(truePower));
    }

    public void stop() {
        leftSpark.set(0);
        rightSpark.set(0);
    }

}