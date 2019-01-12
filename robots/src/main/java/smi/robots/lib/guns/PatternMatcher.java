package smi.robots.lib.guns;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import smi.robots.lib.MyUtils;
import smi.robots.lib.firepower.FirePower;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;
import smi.robots.lib.intel.Observation;

public class PatternMatcher extends Gun {
    private static final int PATTERN_LENGTH = 8;
    private double fireX;
    private double fireY;

    public PatternMatcher(AdvancedRobot robot, FirePower firepower) {
        super(robot, firepower, Color.WHITE);
    }

    public String getName() {
        return getClass().getName();
    }

    @Override
    public VirtualBullet fire(Intel enemy, double firepower) {
        return new VirtualBullet(getName(), enemy.getName(), robot.getX(),
            robot.getY(), fireX, fireY, robot.getTime(), firepower);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            onScannedRobot((ScannedRobotEvent)e);
        }
    }

    private void onScannedRobot(ScannedRobotEvent e) {
        Intel enemy = RobotManager.getEnemy(e.getName());
        if (!RobotManager.isTarget(e.getName())) {
            return;
        }

        double fp = firepower.getPower();
        // Worse case is simple circular targeting, so start with that.
        Point2D.Double point = new Point2D.Double();
        point = MyUtils.computeFutureBearingCircular(robot.getX(), robot.getY(),
            enemy.getAngularVelocity(), enemy.getVelocity(), enemy.getX(), enemy.getY(),
            enemy.getHeadingRadians(),
            fp, robot.getBattleFieldWidth(), robot.getBattleFieldHeight());
        fireX = point.x;
        fireY = point.y;

        List<Observation> obs = enemy.getObservations();

        // Find the index where our pattern starts.
        int maxIdx = obs.size() - 1;
        int patternStartIdx = maxIdx;
        do {
            if (patternStartIdx == 0 ||
                obs.get(patternStartIdx) == null ||
                obs.get(patternStartIdx-1) == null) {
                break;
            }
            patternStartIdx--;
        }while (patternStartIdx > maxIdx - PATTERN_LENGTH);

        if (patternStartIdx == maxIdx) {
            return; // We don't have enough to make a pattern
        }

        // Now look through the data to find the best match
        double bestScore = Double.MAX_VALUE;
        int bestIdx = -1;
        int patternLen = obs.size() - patternStartIdx;
        int upperBound = obs.size()-patternLen * 2;
        L1: for (int i = 0; i < upperBound; i++) {
            double score = 0;
            for (int p = 0; p < patternLen; p++) {
                Observation o = obs.get(i+p);
                if (o == null) {
                    continue L1;
                }
                Observation op = obs.get(patternStartIdx+p);
                score += Math.abs(op.getVelocity() - o.getVelocity());
                score += Math.abs(op.getAngularVelocity() - o.getAngularVelocity());
            }
            if (score <= bestScore && obs.get(i+patternLen) != null) {
                bestScore = score;
                bestIdx = i+patternLen;
            }
        }

        if (bestIdx == -1) { // All the sprints must be too short
            return;
        }

        // Now that we have found the best match, let's apply it to the current
        // Enemy's location to predict where they will be in the future.
        for (int x = 0; x < 20; x++) {
            double timeToImpact = Math.round(Math.sqrt(Math.pow(fireX-robot.getX(),2) +
                Math.pow(fireY-robot.getY(),2))/Rules.getBulletSpeed(fp));
            double newHeading = enemy.getHeadingRadians();
            fireX = enemy.getX();
            fireY = enemy.getY();
            Observation o = null;
            Observation o2 = null;
            for (int i = 0; i < (int)timeToImpact; i++) {
                o2 = obs.get(Math.min(bestIdx+i, obs.size()-1)); 
                if (o2 != null) o = o2;

                if (o.getAngularVelocity() > 0.000001) { // Circular
                    double radius = o.getVelocity()/o.getAngularVelocity();
                    fireY += (Math.sin(newHeading + o.getAngularVelocity()) * radius) - 
                                  (Math.sin(newHeading) * radius);
                    fireX += (Math.cos(newHeading) * radius) - 
                                  (Math.cos(newHeading + o.getAngularVelocity()) * radius);
                }
                else { // Linear
                    fireX += Math.sin(newHeading) * o.getVelocity();
                    fireY += Math.cos(newHeading) * o.getVelocity();
                }
                newHeading += o.getAngularVelocity();
            }
        }

        // Make sure we don't aim to a point outside the battlefield.
        double margin = robot.getWidth()/2;
        fireX = Math.max(Math.min(fireX, robot.getBattleFieldWidth()-margin), margin);
        fireY = Math.max(Math.min(fireY, robot.getBattleFieldHeight()-margin), margin);
    }
}
