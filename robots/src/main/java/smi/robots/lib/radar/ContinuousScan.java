package smi.robots.lib.radar;

import java.util.HashSet;
import java.util.Set;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public class ContinuousScan extends WideLock {
    private Set<String> scanned = new HashSet<String>();
    private long lastScanTime;
    private int direction = 1;

    public ContinuousScan(AdvancedRobot robot) {
        super(robot);
    }
    
    @Override
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
        else if (e instanceof RobotDeathEvent) {
            onRobotDeath((RobotDeathEvent)e);
        }
    }

    @Override
    protected void onScannedRobot(ScannedRobotEvent e) {
        if (robot.getOthers() == 1) {
            super.onScannedRobot(e);
        }
        else {
            Intel enemy = RobotManager.getEnemy(e.getName());
            scanned.add(enemy.getName());
            if (robot.getTime() - lastScanTime > 4) {
                scanned.clear();
                scanned.add(enemy.getName());
            }
            else if (scanned.size() >= robot.getOthers()) {
                robot.setTurnRadarRight(-robot.getRadarTurnRemaining());
                direction *= -1;
                scanned.clear();
                scanned.add(enemy.getName());
            }
            
            lastScanTime = robot.getTime();
        }
    }
    
    protected void onRobotDeath(RobotDeathEvent e) {
        scanned.remove(e.getName());
    }
}
