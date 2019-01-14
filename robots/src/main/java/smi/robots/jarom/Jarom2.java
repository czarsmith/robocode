package smi.robots.jarom;

import java.awt.Color;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Jarom2 extends Robot {
  @Override
  public void run() {
    setColors(Color.BLACK, Color.BLACK, Color.BLACK);
    setBulletColor(Color.WHITE);

    while (true) {
      ahead(100)
      turngunleft(360)
      
      
     
     
      
      
      
      
      
      
      
      
    }
  }
  
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    fire(3);
    fire(2);
    
  }
}
