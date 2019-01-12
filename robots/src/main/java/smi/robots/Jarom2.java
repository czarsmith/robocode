package smi.robots;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Jarom2 extends Robot {
  @Override
  public void run() {
    setColors(Color.BLACK, Color.BLACK, Color.BLACK);
    setBulletColor(Color.WHITE);

    while (true) {
      ahead(100);
      back(90);
      turnRight(180);
      turnLeft(80);
      turnGunLeft(100);
      back(90);
      ahead(100);
      turnGunRight(380);
      ahead(100);turnGunRight(120);
      back(380);
      
      
      
    }
  }
  
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    fire(2);
     
    
  }
}
