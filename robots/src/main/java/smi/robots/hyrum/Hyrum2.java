package smi.robots.hyrum;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Hyrum2 extends Robot {
  @Override
  public void run() {
    setColors(Color.BLACK, Color.MAGENTA, Color.MAGENTA);
    setBulletColor(Color.MAGENTA);

    while (true) {
    	turnGunRight(360);
        
        
     }
  }
  
  
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    fire(3);
 
  }
}
