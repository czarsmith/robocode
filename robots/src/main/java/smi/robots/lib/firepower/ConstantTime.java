package smi.robots.lib.firepower;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public class ConstantTime extends FirePower {
    private double bulletFlightTime;

    public ConstantTime(AdvancedRobot robot) {
        this(robot, 15);
    }

    public ConstantTime(AdvancedRobot robot, double bulletFlightTime) {
        super(robot);
        this.bulletFlightTime = bulletFlightTime;
    }

    @Override
    public void onEvent(Event e) {
        super.onEvent(e);
        
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
    }
    
    private void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        if (RobotManager.isTarget(enemy)) {
            double bulletSpeed = enemy.getDistance() / bulletFlightTime; 
            baselinePower = Math.min((enemy.getEnergy() + 2) / 6,
                (20 - bulletSpeed) / 3);
            baselinePower = Math.min(Rules.MAX_BULLET_POWER, Math.max(0, baselinePower));
            actualPower = baselinePower;
            if (actualPower > robot.getEnergy()) actualPower = Rules.MIN_BULLET_POWER;
            actualPower = Math.max(Rules.MIN_BULLET_POWER, Math.min(actualPower, Rules.MAX_BULLET_POWER));
        }
    }
}
