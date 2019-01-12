package smi.robots.lib.radar;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import smi.robots.lib.intel.RobotManager;

public class NarrowLock extends Radar {
    public NarrowLock(AdvancedRobot robot) {
        super(robot);
        robot.setAdjustRadarForGunTurn(true);
        robot.setAdjustRadarForRobotTurn(true);
        robot.turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    @Override
    public void doTurn() {
        robot.scan();
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
        else if (e instanceof RobotDeathEvent) {
            onRobotDeath((RobotDeathEvent)e);
        }
    }

    private void onScannedRobot(ScannedRobotEvent e) {
        double radarTurn = robot.getHeadingRadians() +
            RobotManager.getEnemy(e.getName()).getBearingRadians() -
            robot.getRadarHeadingRadians();
        robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
    }
    
    private void onRobotDeath(RobotDeathEvent e) {
        if (RobotManager.isTarget(e.getName())) {
            robot.turnRadarRightRadians(Math.PI * 2);
        }
    }
}
