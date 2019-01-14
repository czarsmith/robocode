package smi.robots.hyrum;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

public class HyrumRobotOne extends Robot {
	private double direction = 1;
	public void run() {	
		setBulletColor(Color.MAGENTA);
	    setBodyColor(Color.RED);
	    setRadarColor(Color.BLACK);
	    setScanColor(Color.RED);
		while (true) { 
			turnGunRight(360);
			ahead(300);
			turnGunRight(360);
			back(300);
			
			
			
		}   
	}

	public void onScannedRobot(ScannedRobotEvent e) {
        fire(3);	turnGunLeft(10); turnGunRight(10); turnGunLeft(10); turnGunRight(10);  
	} 
	
	public void onHitByBullet(HitByBulletEvent event) {
	    
	}

}
        
        

    


   
    	
    






