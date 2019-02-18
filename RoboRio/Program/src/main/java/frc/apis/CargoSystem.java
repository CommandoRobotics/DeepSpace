package frc.apis;

public class CargoSystem {

    private CargoIntake cargoIntake;
    private CargoConveyorBelt cargoConveyorBelt;
    private CargoOutput cargoOutput;

    private Communications communications;
    private static final int FIRST_DIGITAL_INPUT_PORT = 1;
    private static final int SECOND_DIGITAL_INPUT_PORT = 2;

    public CargoSystem(CargoIntake cargoIntake, CargoConveyorBelt cargoConveyorBelt, CargoOutput cargoOutput, Communications communications) {
        this.cargoIntake = cargoIntake;
        this.cargoConveyorBelt = cargoConveyorBelt;
        this.cargoOutput = cargoOutput;
        this.communications = communications;
    }

    public void update() {
        cargoIntake.update();
        cargoConveyorBelt.update();
        cargoOutput.update();
    }

    public void expelAllContents(double power) {
        power = Math.abs(power);
        setIntake(power);
        setConveyorBelt(power);
        setCargoOutput(power);
    }

    public void intake(double power) {
        power = Math.abs(power);
        setIntake(-power);
        setConveyorBelt(-power);
        setCargoOutput(0);
    }

    public void intake(double intakePower, double conveyorBeltPower) {
        setIntake(-Math.abs(intakePower));
        setConveyorBelt(-Math.abs(conveyorBeltPower));
        setCargoOutput(0);
    }

    public void shoot(double power) {
        power = Math.abs(power);
        setIntake(0);
        setConveyorBelt(0);
        setCargoOutput(-power);
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
    }

}