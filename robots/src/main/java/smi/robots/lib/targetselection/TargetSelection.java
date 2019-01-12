package smi.robots.lib.targetselection;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public abstract class TargetSelection {
    protected AdvancedRobot robot;
    private double targetValue;
    private double impedence = 1.25;
    protected Intel currentTarget;

    public TargetSelection(AdvancedRobot robot) {
        this.robot = robot;
    }

    public void doTurn() {}
    
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
        else if (e instanceof RobotDeathEvent) {
            onRobotDeath((RobotDeathEvent)e);
        }
    }

    public void setImpedance(double value) {
        this.impedence = value;
    }
    
    protected void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        double value = getEnemyValue(robot, enemy); 
        if (value > targetValue * impedence && currentTarget != enemy) {
            currentTarget = enemy;
            RobotManager.setTarget(currentTarget);
            System.out.println("New Target: " + enemy.getName());
        }

        if (RobotManager.isTarget(enemy)) {
            targetValue = value;
        }
    }

    protected void onRobotDeath(RobotDeathEvent e) {
        if (RobotManager.isTarget(e.getName())) {
            RobotManager.setTarget(null);
            targetValue = Double.NEGATIVE_INFINITY;
        }
    }

    public abstract double getEnemyValue(AdvancedRobot robot, Intel data);
}
