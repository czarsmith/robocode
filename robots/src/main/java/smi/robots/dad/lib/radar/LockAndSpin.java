package smi.robots.dad.lib.radar;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;

public class LockAndSpin extends Radar {
    private double scanArc = 36;
    private double radarTurnTime =
        Math.PI * 2 / Rules.RADAR_TURN_RATE_RADIANS;
    private boolean seeking = false;
    private long seekStartTime = 0;

    public LockAndSpin(AdvancedRobot robot) {
        this(robot, 36);
    }

    public LockAndSpin(AdvancedRobot robot, double scanArc) {
        super(robot);
        this.scanArc = scanArc;
        robot.setAdjustRadarForGunTurn(true);
        robot.setAdjustRadarForRobotTurn(true);
    }

    @Override
    public void doTurn() {
        double coolingTime = robot.getGunHeat() / robot.getGunCoolingRate();

        if (!seeking && coolingTime > radarTurnTime && robot.getOthers() > 1) {
            seeking = true;
            robot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        }
        else if (robot.getRadarTurnRemaining() == 0) {
            robot.setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
    }
    
    private void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());

        if (seeking) {
            if (robot.getTime() - seekStartTime > radarTurnTime) {
                seeking = false;
            }
        }
        
        if (!seeking && RobotManager.isTarget(enemy)) {
            double radarTurn = Utils.normalRelativeAngle(
                robot.getHeadingRadians() + enemy.getBearingRadians()
                - robot.getRadarHeadingRadians());
    
            // Distance we want to scan from middle of enemy to either side
            double extraTurn = Math.min(Math.atan(scanArc / enemy.getDistance()), Math.PI/4.0);
    
            robot.setTurnRadarRightRadians(radarTurn + (radarTurn < 0 ? -extraTurn : extraTurn));
        }
    }
}
