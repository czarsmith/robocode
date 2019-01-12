package smi.robots.dad;

import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class TargetPractice extends Robot {
	private double direction = 1;

	public void run() {
		while (true) {
			ahead(105 * direction);
			turnGunRight(370);
		}
	}
	
	public void onHitWall(HitWallEvent e) { direction *= -1; }
	public void onScannedRobot(ScannedRobotEvent e) {	fire(3); }
}
