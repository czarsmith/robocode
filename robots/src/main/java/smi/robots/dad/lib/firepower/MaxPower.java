package smi.robots.dad.lib.firepower;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;

public class MaxPower extends FirePower {
    public MaxPower(AdvancedRobot robot) {
        super(robot);
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
            actualPower = Math.min(baselinePower, (enemy.getEnergy() + 2) / 6);
            if (actualPower > robot.getEnergy()) {
                actualPower = Rules.MIN_BULLET_POWER;
            }
        }
    }
}
