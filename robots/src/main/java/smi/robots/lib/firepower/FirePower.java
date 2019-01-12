package smi.robots.lib.firepower;

import robocode.AdvancedRobot;
import robocode.Event;

public class FirePower {
    protected AdvancedRobot robot;
    protected double baselinePower = 3;
    protected double actualPower = 3;

    public FirePower(AdvancedRobot robot) {
        this.robot = robot;
    }

    public void doTurn() {}
    public void onEvent(Event e) {}

    public double getPower() {
        return actualPower;
    }
}
