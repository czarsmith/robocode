package smi.robots.dad;

import static robocode.util.Utils.normalRelativeAngle;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class Sparky300 extends AdvancedRobot {
	private String target;
	private double targetValue = Double.MAX_VALUE;
	private double direction = 1;

    public void run() {
    	setColors(Color.RED, Color.BLACK, Color.RED);
    	setBulletColor(Color.RED);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        setTurnRadarRight(Double.POSITIVE_INFINITY);

        while(true) {
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
    	// Target Selection
    	if (e.getDistance() < targetValue) {
    		target = e.getName();
    	}

    	if (e.getName().equals(target)) {
    		// Radar
    		setTurnRadarLeftRadians(getRadarTurnRemainingRadians());

    		// Targeting
        	targetValue = e.getDistance();
    		double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        	double targetHeading = e.getHeadingRadians();
    		double d = e.getVelocity() * e.getDistance() / 11;
        	setTurnGunRightRadians(normalRelativeAngle(Math.atan(
        		(d * Math.sin(targetHeading - absoluteBearing)) /
        		((d * Math.cos(targetHeading - absoluteBearing)) + e.getDistance())) +
        		absoluteBearing - getGunHeadingRadians()));
        	setFire(3);

        	// Forward/Backward
            if (Math.abs(getDistanceRemaining()) < 10) {
            	direction *= -1;
            	setAhead(direction * 300);
            }

            // Turn.  We try to keep the target at a bearing of +/- 90 degrees.
            // An approximation of wall smoothing is also employed to keep us off the walls
            // as well as some overturn to close in on the target.
            double turnDeg = Math.abs(e.getBearing()) - 90 +
            	((e.getDistance() - 200) / 5 ) * direction;
            setTurnRight(e.getBearing() >= 0 ? turnDeg : -turnDeg);
    	}
    }

    public void onRobotDeath(RobotDeathEvent e) {
    	if (e.getName().equals(target)) {
    		targetValue = Double.MAX_VALUE;
    	}
    }
}
