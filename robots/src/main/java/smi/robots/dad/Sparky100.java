package smi.robots.dad;

import java.awt.Color;
import java.awt.Graphics2D;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Sparky100 extends AdvancedRobot {
    public void run() {
        setColors(Color.ORANGE, Color.WHITE, Color.ORANGE);
        setBulletColor(Color.ORANGE);
        setAdjustRadarForGunTurn(true);
        setTurnRadarRight(Double.POSITIVE_INFINITY);

        getGraphics().drawOval(0,0,100,100);

        while(true) {
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
    	setTurnRadarRightRadians(2 * Utils.normalRelativeAngle(
    	    	getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians()));
    	setTurnRight(e.getBearing());
    	setAhead(e.getDistance()-65);
    	if (Math.abs(e.getBearing()) < 5) setFire(3);
    }

    public void onRobotDeath(RobotDeathEvent e) {
    	setTurnRadarRight(360);
    }

    public void onPaint(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.drawOval(0,0, 100, 100);
    }
}
