package smi.robots.lib.guns;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.Event;
import smi.robots.lib.firepower.FirePower;
import smi.robots.lib.intel.Intel;

public abstract class Gun {
    protected AdvancedRobot robot;
    protected Color color;
    protected FirePower firepower;

    public Gun(AdvancedRobot robot, FirePower firepower) {
        this(robot, firepower, Color.WHITE);
    }

    public Gun(AdvancedRobot robot, FirePower firepower, Color c) {
        this.robot = robot;
        this.firepower = firepower;
        this.color = c;
        robot.setAdjustGunForRobotTurn(true);
    }

    public abstract String getName();
    public abstract VirtualBullet fire(Intel enemy, double firePower);

    public void onEvent(Event e) {}
    
    public Color getColor() {
        return color;
    }
}
