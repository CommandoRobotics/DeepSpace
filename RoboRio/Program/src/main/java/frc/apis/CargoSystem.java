package frc.apis;

public class CargoSystem {

    private CargoIntake cargoIntake;
    private CargoConveyorBelt cargoConveyorBelt;
    private CargoOutput cargoOutput;
    private ArmWinch armWinch;

    private Communications communications;

    public CargoSystem(CargoIntake cargoIntake, CargoConveyorBelt cargoConveyorBelt, CargoOutput cargoOutput, ArmWinch armWinch,
        Communications communications) {
        this.cargoIntake = cargoIntake;
        this.cargoConveyorBelt = cargoConveyorBelt;
        this.cargoOutput = cargoOutput;
        this.armWinch = armWinch;

        this.communications = communications;
    }

    public void update() {
        cargoIntake.update();
        cargoConveyorBelt.update();
        cargoOutput.update();
        armWinch.update();
    }

    public void deployIntake(double power) {
        armWinch.deploy(power);
    }

    public void retractIntake(double power) {
        armWinch.retract(power);
    }

    public void expelAllContents(double power) {
        power = Math.abs(power);
        setIntake(-power);
        setConveyorBelt(power);
        setCargoOutput(-power);
        armWinch.stop();
    }

    public void intake(double intakePower, double conveyorBeltPower) {
        intakePower = Math.abs(intakePower);
        conveyorBeltPower = Math.abs(conveyorBeltPower);
        setIntake(-intakePower);
        setConveyorBelt(conveyorBeltPower);
        System.out.println("Cargo Digital Ports: " + communications.getDigitalPortInput(0) + " " + !communications.getDigitalPortInput(1));
        if(communications.getDigitalPortInput(0) && !communications.getDigitalPortInput(1)) {
            setCargoOutput(-0.50);
        } else if(!communications.getDigitalPortInput(1)) {
            setCargoOutput(-0.15);
        } else {
            setCargoOutput(0);
        }
        armWinch.stop();
    }

    public void shoot(double power) {
        power = Math.abs(power);
        setIntake(0);
        setConveyorBelt(0);
        setCargoOutput(-power);
        armWinch.stop();
    }

    public void setIntake(double power) {
        if(power > 0) cargoIntake.pullCargo(power);
        else cargoIntake.pushCargo(power);
    }

    public void setConveyorBelt(double power) {
        if(power > 0) cargoConveyorBelt.pullCargo(power);
        else cargoConveyorBelt.pushCargo(power);
    }

    public void setCargoOutput(double power) {
        if(power > 0) cargoOutput.pushCargo(power);
        else cargoOutput.pullCargo(power);
    }

    public void stop() {
        cargoIntake.stop();
        cargoConveyorBelt.stop();
        cargoOutput.stop();
        armWinch.stop();
    }

}