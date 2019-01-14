package smi.robots.hyrum;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Hyrum1 extends Robot {
  @Override
  public void run() {
    setColors(Color.MAGENTA, Color.MAGENTA, Color.MAGENTA);
    setBulletColor(Color.MAGENTA);

    while (true) {
      turnGunRight(360);
    }
  }
  
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    fire(3); turnGunLeft(10); turnGunRight(10); turnGunLeft(10); turnGunLeft(10); turnGunRight(10); turnGunLeft(10); 
  }
}
