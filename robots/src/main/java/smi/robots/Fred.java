package smi.robots;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Fred extends AdvancedRobot {
  @Override
  public void run() {
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    
    while (true) {
      setTurnRight(360);
      setAhead(500);
      
      if (Utils.isNear(getRadarTurnRemaining(), 0)) {
        setTurnRadarRight(720);
      }

      execute();
    }
  }
  
  @Override
  public void onScannedRobot(ScannedRobotEvent event) {
    setTurnRadarRightRadians(2 * Utils.normalRelativeAngle(event.getBearingRadians() + getHeadingRadians() - getRadarHeadingRadians()));
    setTurnGunRightRadians(Utils.normalRelativeAngle(event.getBearingRadians() + getHeadingRadians() - getGunHeadingRadians()));
    setFire(3);
  }
}
