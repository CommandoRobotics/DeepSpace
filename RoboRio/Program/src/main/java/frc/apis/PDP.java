package frc.apis;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PDP extends PowerDistributionPanel {

    public PDP() {
        super();
    }

    public PDP(int canID) {
        super(canID);
    }

    @Override
    public double getTemperature() {
        return super.getTemperature() * 1.8 + 32;
    }

}