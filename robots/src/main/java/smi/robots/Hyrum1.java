package smi.robots;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Hyrum1 extends Robot {
  @Override
  public void run() {
    setColors(Color.MAGENTA, Color.MAGENTA, Color.MAGENTA);
    setBulletColor(Color.MAGENTA);

    while (true) {
      ahead(100);
    }
  }
  
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    fire(3);
  }
}
