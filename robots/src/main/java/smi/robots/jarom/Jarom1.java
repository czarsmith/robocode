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
      import targeatlok
    
      turngunright(360)
      ahead(100)
      turngun(360)
      
      
      
      
      
       
      
      
      
      
    }
  }
  
  @Override
  public void onScannedRobotstuk(ScannedRobotEvent event) {
       
    fire(3)
    
  }
}
