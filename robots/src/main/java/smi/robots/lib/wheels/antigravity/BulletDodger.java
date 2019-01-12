package smi.robots.lib.wheels.antigravity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;
import smi.robots.lib.wheels.antigravity.forces.Force;
import smi.robots.lib.wheels.antigravity.forces.LinearTravelingTangentalForce;
import smi.robots.lib.wheels.antigravity.forces.PointForce;

public abstract class BulletDodger extends TargetCentricAntigravity {
    public static double DEFAULT_BULLET_MAGNITUDE = 600000;
    public static double DEFAULT_BULLET_DECAY = 2;

    protected List<LinearTravelingTangentalForce> liveBullets;
    protected double bulletStrength;
    protected double bulletDecay;

    public BulletDodger(AdvancedRobot robot) {
        this(robot, DEFAULT_BULLET_MAGNITUDE, DEFAULT_BULLET_DECAY);
    }

    public BulletDodger(AdvancedRobot robot, double bulletStrength, double bulletDecay) {
        super(robot);
        this.bulletStrength = bulletStrength;
        this.bulletDecay = bulletDecay;
        setOptimumRange(400);
        liveBullets = new ArrayList<LinearTravelingTangentalForce>();
    }

    @Override
    public Collection<Force> getForces() {
        Collection<Force> ret = super.getForces();

        // Remove expired bullets
        for (Iterator<LinearTravelingTangentalForce> iter = liveBullets.iterator(); iter.hasNext();) {
            LinearTravelingTangentalForce f = iter.next();
            if (f instanceof PointForce && robot.getTime() > ((PointForce)f).getExpires()) {
                iter.remove();
            }
        }

        ret.addAll(liveBullets);

        return ret;
    }

    @Override
    public void onEvent(Event e, boolean takeAction) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }

        super.onEvent(e, takeAction);
    }

    protected void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        double de = enemy.getPreviousEnergy() - enemy.getEnergy();
        if (de > 0 && de <= 3 && !hitWall(robot, enemy, de) &&
            RobotManager.isTarget(enemy) &&
            enemy.getPreviousTime() >= robot.getTime() - 1) {
            
            createBullet(enemy);
        }
    }

    /**
     * Create a force to represent the bullet.
     */
    protected abstract void createBullet(Intel enemy);

    protected boolean hitWall(AdvancedRobot robot, Intel enemy, double firePower) {
        if (enemy.getVelocity() == 0 && enemy.getPreviousVelocity() != 0) {
            double wallHitDamage = Rules.getWallHitDamage(enemy.getPreviousVelocity()); 
            return Utils.isNear(firePower, wallHitDamage);
        }
        return false;
    }
}
