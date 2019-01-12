package smi.robots;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

public class HyrumRobotOne extends Robot {
	public void run() {	
		setBulletColor(Color.GREEN);
	    setBodyColor(Color.RED);
	    setRadarColor(Color.BLACK);
	    setScanColor(Color.RED);
	    turnGunRight(360);
		while (true) { 
			turnRight(90);
			turnGunRight(360);
			turnGunRight(360);
			ahead(100);
			turnGunRight(360);
			
		}   
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		fire(2); turnGunRight(0);
		
		
		
	} 
	public void onHitByBullet(HitByBulletEvent event) {
		turnRight(90);
		back(100);
	}

    public void onHitRobot(HitRobotEvent event) {
        
        

    }

}
   
    	
    






