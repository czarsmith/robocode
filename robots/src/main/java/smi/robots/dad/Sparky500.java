package smi.robots.dad;

import static robocode.util.Utils.getRandom;
import static robocode.util.Utils.normalRelativeAngle;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class Sparky500 extends AdvancedRobot {
	private static final double TRAVEL = 300;
	private static final double BUFFER = 225;
	private String target;
	private double targetValue = Double.MAX_VALUE;
	private double targetLastHeading;
	private double firePower;
	private double direction = 1;

    public void run() {
    	setColors(Color.YELLOW, Color.BLACK, Color.YELLOW);
    	setBulletColor(Color.YELLOW);
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

    		// Targeting and firepower
        	double targetAngularVelocity = e.getHeadingRadians() - targetLastHeading; // Assume 1 time unit
        	double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        	targetLastHeading = e.getHeadingRadians();
        	targetValue = e.getDistance();
        	firePower = Math.min((e.getEnergy() + 2) / 6, 3);
        	if (firePower > getEnergy()) firePower = 0.1;
        	// Compute gun heading
        	double targetHeading;
        	double bulletDist = 20 - 3 * firePower;
        	double time = e.getDistance() / bulletDist;
        	double d,dx=0,dy=0;
        	int i=0;
        	while (i++ < 20) {
        		targetHeading = e.getHeadingRadians() + targetAngularVelocity * time / 2;
        		d = e.getVelocity() * time;
    			dx = (d * Math.sin(targetHeading - absoluteBearing));
    			dy = (d * Math.cos(targetHeading - absoluteBearing));
    			time = (dy + e.getDistance()) / bulletDist;
        	}
        	setTurnGunRightRadians(normalRelativeAngle(Math.atan(dx / (dy + e.getDistance())) +
        			absoluteBearing - getGunHeadingRadians()));
        	setFire(firePower);

        	// Forward/Backward
            if (Math.abs(getDistanceRemaining()) < 10) {
            	direction *= -1;
            	setAhead(direction * getRandom().nextDouble() * TRAVEL);
            }

            // Turn.  We try to keep the target at a bearing of +/- 90 degrees.
            // An approximation of wall smoothing is also employed to keep us off the walls
            // as well as some overturn to close in on the target.
        	double xbw = getBattleFieldWidth() - getX();
        	double ybh = getBattleFieldHeight() - getY();
        	double minxy = Math.min(Math.min(xbw, ybh), Math.min(getX(), getY()));
            double turnDeg = Math.abs(e.getBearing()) - 90 +
            	((e.getDistance() - BUFFER) / 5 + Math.max(0, 110 - minxy)) * direction;
            setTurnRight(e.getBearing() >= 0 ? turnDeg : -turnDeg);
    	}
    }

    public void onRobotDeath(RobotDeathEvent e) {
    	if (e.getName().equals(target)) {
    		targetValue = Double.MAX_VALUE;
    	}
    }
}
