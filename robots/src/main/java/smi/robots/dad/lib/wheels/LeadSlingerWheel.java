package smi.robots.dad.lib.wheels;

import java.util.Collection;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.wheels.antigravity.BasicAntigravity;
import smi.robots.dad.lib.wheels.antigravity.forces.Force;
import smi.robots.dad.lib.wheels.antigravity.forces.PointForce;

/**
 * This wheel was built for the Millard West High School skills challenge.
 * It will find a corner and stay there.
 */
public class LeadSlingerWheel extends BasicAntigravity {
    private static final double CORNER_FORCE = 2000000;
    private static final double CORNER_DECAY = 2.5;
    private static final double WALL_FORCE = 80000;
    private static final double ENEMY_FORCE = 2000000;
    private static final double BUFFER = 50;

    public LeadSlingerWheel(AdvancedRobot robot) {
        super(robot, WALL_FORCE, 2, 1000);
    }

    protected double getMagnitude(Intel intel) {
        return ENEMY_FORCE;
    }

    @Override
    public Collection<Force> getForces() {
        Collection<Force> ret = super.getForces();

        // Add attractive corner forces
        ret.add(new PointForce(BUFFER, BUFFER, CORNER_FORCE, CORNER_DECAY, 1));
        ret.add(new PointForce(BUFFER, robot.getBattleFieldHeight()-BUFFER, CORNER_FORCE, CORNER_DECAY, 1));
        ret.add(new PointForce(robot.getBattleFieldWidth()-BUFFER, robot.getBattleFieldHeight()-BUFFER, CORNER_FORCE, CORNER_DECAY, 1));
        ret.add(new PointForce(robot.getBattleFieldWidth()-BUFFER, BUFFER, CORNER_FORCE, CORNER_DECAY, 1));
        return ret;
    }
}