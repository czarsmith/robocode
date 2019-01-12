package smi.robots.jarom;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Jarom1 extends Robot {
  @Override
  public void run() {
    setColors(Color.BLUE, Color.GREEN, Color.WHITE);
    setBulletColor(Color.GREEN);

    while (true) {
      ahead(100);
      back(90);
      turnRight(180);
      turnLeft(80);
      turnGunLeft(100);
      turnGunRight(100);
      turnGunLeft(380);
      ahead(380);
      back(240);
      ahead(240);
      turnGunRight(380);
      turnGunLeft(240);
      
      
      
      
    }
  }
  
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    fire(2);
    fire(2);
    
    
  }
}
