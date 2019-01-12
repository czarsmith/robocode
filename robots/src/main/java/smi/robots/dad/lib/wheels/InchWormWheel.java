package smi.robots.dad.lib.wheels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.intel.Intel;
import smi.robots.dad.lib.intel.RobotManager;
import smi.robots.dad.lib.wheels.antigravity.Antigravity;
import smi.robots.dad.lib.wheels.antigravity.forces.Force;
import smi.robots.dad.lib.wheels.antigravity.forces.LinearTravelingRadialForce;
import smi.robots.dad.lib.wheels.antigravity.forces.PointForce;

public class InchWormWheel extends Antigravity {
    public static final double MAGNITUDE = 800000;
    public static final double DECAY = 1.7;

    public static final double ENEMY_MAGNITUDE = 800000;
    public static final double ENEMY_DECAY = 2;

    public static final long ON_TIME = 12;
    public static final long OFF_TIME = 8;
    
    public static final int WALL_MARGIN = 25;

    private boolean initializing = true;
    private boolean moveRight = true;
    private boolean on = false;
    private long switchTime = 15;

    private double fieldWidth = 0;
    private double fieldHeight = 0;

    protected PointForce toForce;
    protected Map<String, LinearTravelingRadialForce> repulsiveForces;

    public InchWormWheel(AdvancedRobot robot) {
        super(robot);
        repulsiveForces = new HashMap<String, LinearTravelingRadialForce>();
        toForce = new PointForce(0, 0, MAGNITUDE, DECAY);
        toForce.setPolarity(1);
        fieldHeight = robot.getBattleFieldHeight();
        fieldWidth = robot.getBattleFieldWidth();
    }

    @Override
    public void doTurn(boolean takeAction) {
        super.doTurn(takeAction);

        if (initializing && RobotManager.getMyRobot().getY() > fieldHeight - WALL_MARGIN*2) {
            initializing = false;
            on = true;
            switchTime = RobotManager.getMyRobot().getTime() + ON_TIME;
        }
        else if (moveRight && RobotManager.getMyRobot().getX() > fieldWidth - WALL_MARGIN) {
            moveRight = false;
        }
        else if (!moveRight && RobotManager.getMyRobot().getX() < 20) {
            moveRight = true;
        }

        if (!initializing) {
            if (RobotManager.getMyRobot().getTime() > switchTime) {
                switchTime = RobotManager.getMyRobot().getTime() + (on ? OFF_TIME : ON_TIME);
                on = !on;
            }
            
            toForce.setMagnitude(on ? MAGNITUDE : 0);
        }
    }

    @Override
    public Collection<Force> getForces() {
        // Update Enemy Forces
        for (Intel enemy : RobotManager.getAllEnemies()) {
            if (enemy.isAlive()) {
                LinearTravelingRadialForce pf = repulsiveForces.get(enemy.getName());
                if (pf == null) {
                    pf = new LinearTravelingRadialForce(robot, enemy, ENEMY_MAGNITUDE, ENEMY_DECAY);
                    repulsiveForces.put(enemy.getName(), pf);
                }

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

        // Update the From/To forces
        if (initializing) { // Moving North
            toForce.setX(RobotManager.getMyRobot().getX());
            toForce.setY(fieldHeight);
            points.addAll(repulsiveForces.values());
        }
        else if (moveRight) { // Moving Right
            toForce.setX(fieldWidth);
            toForce.setY(fieldHeight - WALL_MARGIN);
        }
        else { // Moving Left
            toForce.setX(0);
        }

        points.add(toForce);

        return points;
    }   
}
