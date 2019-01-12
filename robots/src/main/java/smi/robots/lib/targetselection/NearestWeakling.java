package smi.robots.lib.targetselection;

import java.util.HashMap;
import java.util.Map;

import robocode.AdvancedRobot;
import robocode.RobotDeathEvent;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public class NearestWeakling extends TargetSelection {
    private static Map<String, Double> SURVIVAL_SCORES = new HashMap<String, Double>();
    private double maxDistance;
    
    public NearestWeakling(AdvancedRobot robot) {
        this(robot, 1.3);
    }
    
    public NearestWeakling(AdvancedRobot robot, double impedance) {
        super(robot);
        setImpedance(impedance);
        maxDistance = Math.max(robot.getBattleFieldWidth(), robot.getBattleFieldHeight());
    }

    @Override
    public double getEnemyValue(AdvancedRobot robot, Intel data) {
        if (RobotManager.getMyRobot().getOthers() <= 1 || SURVIVAL_SCORES.size() < 2) {
            return maxDistance / data.getDistance();
        }
        else {
            double score = getSurvivalScore(data.getName());
            double minScore = Double.MAX_VALUE;
            double maxScore = Double.MIN_VALUE;
            for (Double s : SURVIVAL_SCORES.values()) {
                if (s < minScore) {
                    minScore = s;
                }
                if (s > maxScore) {
                    maxScore = s;
                }
            }
            return 1 - data.getDistance() / maxDistance + (score - minScore) / (maxScore - minScore);
        }
    }

    protected void onRobotDeath(RobotDeathEvent e) {
        super.onRobotDeath(e);

        // Compute a survival score for this enemy
        Double score = SURVIVAL_SCORES.get(e.getName());
        if (score == null) {
            score = new Double(robot.getOthers());
        }
        else {
            score += robot.getOthers();
        }
        SURVIVAL_SCORES.put(e.getName(), score);
    }
    
    private double getSurvivalScore(String name) {
        Double score = SURVIVAL_SCORES.get(name);
        if (score == null) {
            return 0;
        }
        else {
            return score;
        }
    }
}
