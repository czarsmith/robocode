package smi.robots.dad.lib.firepower;

import java.util.ArrayList;
import java.util.List;

import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import smi.robots.dad.lib.intel.RobotManager;

public class ConstantHitPercent extends FirePower {
    private double pcnt;
    private int maxHistory;
    private List<Bullet> allBullets;
    private List<Bullet> hitBullets;

    public ConstantHitPercent(AdvancedRobot robot) {
        this(robot, 2, 6, 3);
    }
    
    public ConstantHitPercent(AdvancedRobot robot, int hits, int attempts, double startPower) {
        super(robot);
        this.pcnt = (double)hits / attempts;
        maxHistory = attempts;
        baselinePower = startPower;
        actualPower = startPower;
        allBullets = new ArrayList<Bullet>();
        hitBullets = new ArrayList<Bullet>();
    }

    public void adjustBaselinePower() {
        double hitPcnt = getHitPercent();
        if (Utils.isNear(hitPcnt, pcnt)) {
        }
        else if (hitPcnt > pcnt) {
            baselinePower += 0.2;
        }
        else {
            baselinePower -= 0.2;
        }
        baselinePower = Math.min(Rules.MAX_BULLET_POWER,
            Math.max(Rules.MIN_BULLET_POWER, baselinePower));
    }

    public void adjustActualPower(AdvancedRobot robot) {
        actualPower = baselinePower;

        if (RobotManager.getTarget() != null) {
            actualPower = Math.min(actualPower, (RobotManager.getTarget().getEnergy() + 2) / 6);

            // If the enemy is close, use max power.
            if (RobotManager.getTarget().getDistance() < 150) {
                actualPower = Rules.MAX_BULLET_POWER;
            }
        }

        if (actualPower > robot.getEnergy()) actualPower = Rules.MIN_BULLET_POWER;

        actualPower = Math.min(Rules.MAX_BULLET_POWER,
            Math.max(Rules.MIN_BULLET_POWER, actualPower));
    }

    @Override 
    public void doTurn() {
        super.doTurn();
        adjustActualPower(robot);
    }

    @Override
    public void onEvent(Event e) {
        super.onEvent(e);
        
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
        else if (e instanceof BulletHitEvent) {
            recordHit(((BulletHitEvent)e).getBullet());
        }
        else if (e instanceof BulletHitBulletEvent) {
            recordMiss(((BulletHitBulletEvent)e).getBullet());
        }
        else if (e instanceof BulletMissedEvent) {
            recordMiss(((BulletMissedEvent)e).getBullet());
        }
    }

    private void onScannedRobot(ScannedRobotEvent e) {
        adjustActualPower(robot);
    }

    private double getHitPercent() {
        double ret = pcnt;
        if (allBullets.size() == maxHistory) {
            ret = (double)hitBullets.size() / allBullets.size();
        }
        return ret;
    }

    private void recordHit(Bullet b) {
        allBullets.add(b);
        hitBullets.add(b);

        if (allBullets.size() > maxHistory) {
            hitBullets.remove(allBullets.remove(0));
        }
        adjustBaselinePower();
    }

    private void recordMiss(Bullet b) {
        allBullets.add(b);
        if (allBullets.size() > maxHistory) {
            hitBullets.remove(allBullets.remove(0));
        }
        adjustBaselinePower();
    }
}
