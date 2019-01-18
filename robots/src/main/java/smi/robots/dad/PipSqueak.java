package smi.robots.dad;

import java.awt.Color;

import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class PipSqueak extends Robot {
  private double dir = 1;
  private double gunDir = 1;
  private double radarLockAngle = 60; // 1/2 angle to turn the gun
  private double runDist = 40; // distance to drive on each turn
  private double closingAngle = 35; // Angle to use each turn to close the gap
  private double optimumDistance = 400; // Desired distance from target
  
  public void run() {
    setAdjustGunForRobotTurn(true);
    setAllColors(Color.WHITE);
    turnGunRight(370);

    while (true) {
      setAllColors(new Color(
        Utils.getRandom().nextInt(256),
        Utils.getRandom().nextInt(256),
        Utils.getRandom().nextInt(256)));
      ahead(runDist * dir);
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

    turnRight(Utils.normalRelativeAngleDegrees(
      e.getBearing() - 90 // perpendicular
      + (e.getDistance() / optimumDistance * closingAngle) // Close the gap to maintain optimal distance
      * dir)); // Adjust angle for forward/backward movement
  }
}
