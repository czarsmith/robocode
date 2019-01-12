package smi.robots.lib.wheels.antigravity;

import java.util.Collection;

import robocode.AdvancedRobot;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;
import smi.robots.lib.wheels.antigravity.forces.Force;
import smi.robots.lib.wheels.antigravity.forces.RadialForce;

/**
 * This movement adds a repulsive force to the center of the robot cloud in 
 * melee mode.  This essentially just a radial force at the average x,y
 * position of all the enemies.
 */
public class CenterPhobia extends BasicAntigravity {
    private static double DEFAULT_CENTER_DECAY = 1.55;
    private static double DEFAULT_CENTER_STRENGTH = DEFAULT_MAGNITUDE;

    private double centerStrength = DEFAULT_CENTER_STRENGTH;
    private double centerDecay = DEFAULT_CENTER_DECAY;

    public CenterPhobia(AdvancedRobot robot) {
        this(robot, DEFAULT_CENTER_STRENGTH, DEFAULT_CENTER_DECAY);
    }

    public CenterPhobia(AdvancedRobot robot, double centerStrength,
        double centerDecay) {
        super(robot);
        this.centerStrength = centerStrength;
        this.centerDecay = centerDecay;
    }

    public void setCenterStrength(double centerStrength) {
        this.centerStrength = centerStrength;
    }
    
    public void setCenterDecay(double centerDecay) {
        this.centerDecay = centerDecay;
    }
    
    @Override
    public Collection<Force> getForces() {
        Collection<Force> ret = super.getForces();

        double averageX = 0;
        double averageY = 0;
        int enemyCount = 0;
        for (Intel enemy : RobotManager.getAllEnemies()) {
            if (enemy.isAlive()) {
                averageX += enemy.getX();
                averageY += enemy.getY();
                enemyCount++;
            }
        }

        if (enemyCount > 1) {
            averageX /= enemyCount;
            averageY /= enemyCount;

            RadialForce rf = new RadialForce(averageX, averageY, centerStrength, centerDecay);
            ret.add(rf);
        }

        return ret;
    }
}
