package smi.robots.lib.radar;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public class SimpleLock extends Radar {
    public SimpleLock(AdvancedRobot robot) {
        super(robot);
        robot.setAdjustRadarForGunTurn(true);
        robot.setAdjustRadarForRobotTurn(true);
        robot.setTurnRadarRight(Double.POSITIVE_INFINITY);
    }

    /**
     * This algorithm locks the radar on our target.
     */
    @Override
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent )e);
        }
        else if (e instanceof RobotDeathEvent) {
            onRobotDeath((RobotDeathEvent)e);
        }
    }

    private void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        if (RobotManager.isTarget(enemy)) {
            robot.setTurnRadarRightRadians(2 * Utils.normalRelativeAngle(
                robot.getHeadingRadians() + enemy.getBearingRadians() -
                robot.getRadarHeadingRadians()));
        }
    }
    
    private void onRobotDeath(RobotDeathEvent e) {
        if (RobotManager.isTarget(e.getName())) {
            robot.setTurnRadarRight(Double.POSITIVE_INFINITY);
        }
    }
}
