package smi.robots.dad;

import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class PipSqueak extends Robot {
  private double dir = 1;
  private double gunDir = 1;
  private double radarLockAngle = 60;
  
  public void run() {
    setAdjustGunForRobotTurn(true);
    
    turnGunRight(370);

    while (true) {
      ahead(50 * dir);
      turnGunRight(gunDir * radarLockAngle * 2);
    }
  }

  public void onHitWall(HitWallEvent e) { dir *= -1; }

  public void onScannedRobot(ScannedRobotEvent e) {
    if (getGunHeat() == 0) {
      fire(3);
    }
    turnGunRight(gunDir * radarLockAngle);
    gunDir *= -1;
    turnRight(Utils.normalRelativeAngleDegrees(e.getBearing() - 90 + 35 * dir));
  }
}
