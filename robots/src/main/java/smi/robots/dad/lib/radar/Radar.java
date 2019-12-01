package smi.robots.dad.lib.radar;

import robocode.AdvancedRobot;
import robocode.Event;

public abstract class Radar {
    protected AdvancedRobot robot;

    public Radar(AdvancedRobot robot) {
        this.robot = robot;
    }

    public void doTurn() {}
    public void onEvent(Event e) {}
}
