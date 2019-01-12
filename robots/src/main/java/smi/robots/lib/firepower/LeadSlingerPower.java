package smi.robots.lib.firepower;

import robocode.AdvancedRobot;

public class LeadSlingerPower extends FirePower {
    private static final double MIN_POWER = 1;
    private static final double MAX_POWER = 2;
    private static final double STEP = 0.5;
    private double power = MIN_POWER;

    public LeadSlingerPower(AdvancedRobot robot) {
        super(robot);
    }

    public void adjustActualPower(AdvancedRobot robot) {
        actualPower = power;

        if (robot.getGunHeat() == 0) {
            power += STEP;
            if (power > MAX_POWER) {
                power = MIN_POWER;
            }
        }
    }

    @Override 
    public void doTurn() {
        super.doTurn();
        adjustActualPower(robot);
    }
}
