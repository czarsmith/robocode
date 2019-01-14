package smi.robots.dad;

import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class TargetPractice extends Robot {
	private double travel = 105;

	public void run() {
		while (true) {
			ahead(travel);
			turnGunRight(370);
		}
	}
	
	public void onHitWall(HitWallEvent e) { travel *= -1; }
	public void onScannedRobot(ScannedRobotEvent e) {	fire(3); }
}
