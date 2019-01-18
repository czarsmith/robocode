package smi.robots.hyrum;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class HyrumRobotOne extends Robot {
	private double direction = 1;
	public void run() {	
		setBulletColor(Color.MAGENTA);
	    setBodyColor(new Color(10, 0, 10 ));
	    setRadarColor(new Color(10, 0, 10 ));
	    setScanColor(Color.RED);
	    setGunColor(new Color(10, 0, 10 ));
		while (true) { 
		    ahead(75);
			back(75);
			turnGunRight(360);
			
		}   
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		  fire(3);  turnRight(Utils.normalRelativeAngleDegrees(e.getBearing() - 90));   
	} 
	
	public void onHitByBullet(HitByBulletEvent event) {
	    
	}

}
        
     





