package smi.robots.lib.wheels;

import java.awt.Color;

import robocode.AdvancedRobot;

public class RandomWheel extends Wheel {
    private double maxAhead = 500;

    public RandomWheel(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    @Override
    public void doTurn(boolean takeAction) {
        if (takeAction && robot.getDistanceRemaining() == 0) {
            robot.setAhead(500 - Math.random() * maxAhead * 2);
        }
        
        if (takeAction && robot.getTurnRemaining() == 0) {
            robot.setTurnRightRadians(Math.PI - Math.random() * Math.PI* 2);
        }
    }
}
