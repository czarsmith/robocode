package smi.robots.dad.lib.radar;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;

public class WideLock extends Radar {
    private double scanArc = 36;

    public WideLock(AdvancedRobot robot) {
        super(robot);
        robot.setAdjustRadarForGunTurn(true);
        robot.setAdjustRadarForRobotTurn(true);
    }

    @Override
    public void doTurn() {
        if (robot.getRadarTurnRemaining() == 0) {
            robot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
    }

    protected void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        if (RobotManager.isTarget(enemy)) {
            double radarTurn = Utils.normalRelativeAngle(
                robot.getHeadingRadians() + enemy.getBearingRadians()
                - robot.getRadarHeadingRadians());
    
            // Distance we want to scan from middle of enemy to either side
            double extraTurn = Math.min(Math.atan(scanArc / enemy.getDistance()), Math.PI/4.0);
    
            robot.setTurnRadarRightRadians(radarTurn + (radarTurn < 0 ? -extraTurn : extraTurn));
        }
    }
}
