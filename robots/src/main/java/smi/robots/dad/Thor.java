package smi.robots.dad;

import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Thor extends Robot {
  private double dir = 1;

  public void run() {
    while (true) {
      ahead(105 * dir);
      turnGunRight(370);
    }
  }

  public void onHitWall(HitWallEvent e) { dir *= -1; }

  public void onScannedRobot(ScannedRobotEvent e) {
    fire(3);
    turnRight(Utils.normalRelativeAngleDegrees(e.getBearing() - 90));
  }
}
