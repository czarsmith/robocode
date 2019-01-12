package smi.robots.lib.radar;

import robocode.AdvancedRobot;

public class SpinRadar extends Radar {
    public SpinRadar(AdvancedRobot robot) {
        super(robot);
        robot.setAdjustRadarForGunTurn(true);
        robot.setAdjustRadarForRobotTurn(true);
        robot.setTurnRadarRight(Double.POSITIVE_INFINITY);
    }
}
