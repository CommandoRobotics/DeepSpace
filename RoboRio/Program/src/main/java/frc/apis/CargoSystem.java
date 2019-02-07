package frc.apis;

public class CargoSystem {

    private CargoIntake cargoIntake;
    private CargoConveyorBelt cargoConveyorBelt;
    private CargoOutput cargoOutput;

    public CargoSystem(CargoIntake cargoIntake, CargoConveyorBelt cargoConveyorBelt, CargoOutput cargoOutput) {
        this.cargoIntake = cargoIntake;
        this.cargoConveyorBelt = cargoConveyorBelt;
        this.cargoOutput = cargoOutput;
    }

    public void update() {
        cargoIntake.update();
        cargoConveyorBelt.update();
        cargoOutput.update();
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