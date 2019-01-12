package smi.robots.dad.lib.wheels.antigravity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;
import smi.robots.dad.lib.wheels.antigravity.forces.Force;
import smi.robots.dad.lib.wheels.antigravity.forces.LinearTravelingRadialForce;
import smi.robots.dad.lib.wheels.antigravity.forces.RadialForce;

public class BasicAntigravity extends Antigravity {
    public static final double DEFAULT_MAGNITUDE = 800000;
    public static final double DEFAULT_DECAY = 2;
    public static final double DEFAULT_RANGE = 300;

    private double magnitude;
    private double decay;
    private double optimumRange;
    protected List<RadialForce> wallPoints;
    protected Map<String, LinearTravelingRadialForce> repulsiveForces;

    public BasicAntigravity(AdvancedRobot robot) {
        this(robot, DEFAULT_MAGNITUDE, DEFAULT_DECAY, DEFAULT_RANGE);
    }

    public BasicAntigravity(AdvancedRobot robot, double magnitude,
        double decay, double optimumRange) {
        super(robot);

        this.magnitude = magnitude;
        this.decay = decay;
        this.optimumRange = optimumRange;

        wallPoints = new ArrayList<RadialForce>();
        wallPoints.add(new RadialForce(0, 0, magnitude, 2));
        wallPoints.add(new RadialForce(0, 0, magnitude, 2));
        wallPoints.add(new RadialForce(0, 0, magnitude, 2));
        wallPoints.add(new RadialForce(0, 0, magnitude, 2));

        repulsiveForces = new HashMap<String, LinearTravelingRadialForce>();
    }

    public double getMagnitude() {
        return magnitude;
    }
    
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }
    
    public double getDecay() {
        return decay;
    }
    
    public void setDecay(double decay) {
        this.decay = decay;
    }
    
    public double getOptimumRange() {
        return optimumRange;
    }
    
    public void setOptimumRange(double optimumRange) {
        this.optimumRange = optimumRange;
    }

    public Collection<Force> getForces() {
        // Wall points
        wallPoints.get(0).setX(robot.getBattleFieldWidth()).setY(robot.getY());
        wallPoints.get(1).setY(robot.getY());
        wallPoints.get(2).setX(robot.getX()).setY(robot.getBattleFieldHeight());
        wallPoints.get(3).setX(robot.getX());

        for (Intel enemy : RobotManager.getAllEnemies()) {
            if (enemy.isAlive()) {
                LinearTravelingRadialForce pf = repulsiveForces.get(enemy.getName());
                if (pf == null) {
                    pf = new LinearTravelingRadialForce(robot, enemy, magnitude, getDecay(enemy));
                    pf.setOptimumRange(getOptimumRange(enemy));
                    repulsiveForces.put(enemy.getName(), pf);
                }

                pf.setMagnitude(getMagnitude(enemy));
                pf.setOptimumRange(getOptimumRange(enemy));
                pf.setDecay(getDecay(enemy));

                if (enemy.getTime() == robot.getTime()) {
                    pf.update(robot, enemy);
                }
                // else, we don't have any recent information that will help.
            }
            else {
                repulsiveForces.remove(enemy.getName());
            }
        }

        List<Force> points = new ArrayList<Force>();
        points.addAll(wallPoints);
        points.addAll(repulsiveForces.values());

        return points;
    }

    protected double getMagnitude(Intel intel) {
        return getMagnitude();
    }

    protected double getOptimumRange(Intel intel) {
        return getOptimumRange();
    }
    
    protected double getDecay(Intel intel) {
        return getDecay();
    }
}
