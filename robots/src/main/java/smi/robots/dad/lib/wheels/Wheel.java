package smi.robots.dad.lib.wheels;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.Event;

public class Wheel {
    protected AdvancedRobot robot;
    private String name;

    public Wheel(AdvancedRobot robot) {
        this.robot = robot;
    }

    public void doTurn(boolean takeAction) {}
    public void onEvent(Event e, boolean takeAction) {}

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public Color getColor() {
        return Color.BLACK;
    }

    public boolean isMelee() {
        return true;
    }
   
    public boolean isOneOnOne() {
        return true;
    }
}
