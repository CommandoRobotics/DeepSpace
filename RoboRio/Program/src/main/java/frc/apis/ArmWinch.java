package frc.apis;

import edu.wpi.first.wpilibj.Spark;

public class ArmWinch {

    private Spark winch;

    public ArmWinch(int motorPort) {
        this(new Spark(motorPort));
    }

    public ArmWinch(Spark winch) {
        this.winch = winch;

    }

    public void update() {

    }

    public void retract(double power) {
        winch.set(Math.abs(power));
    }

    public void deploy(double power) {
        winch.set(-Math.abs(power));
    }

    public void stop() {
        winch.set(0);
    }

}