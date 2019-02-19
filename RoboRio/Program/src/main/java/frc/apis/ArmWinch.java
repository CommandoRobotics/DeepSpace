package frc.apis;

import edu.wpi.first.wpilibj.Spark;

public class ArmWinch {

    private Spark wench;

    public ArmWinch(int motorPort) {
        this(new Spark(motorPort));
    }

    public ArmWinch(Spark wench) {
        this.wench = wench;
    }

    public void update() {

    }

    public void retract(double power) {
        wench.set(Math.abs(power));
    }

    public void deploy(double power) {
        wench.set(-Math.abs(power));
    }

    public void stop() {
        wench.set(0);
    }

}