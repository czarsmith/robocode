package smi.robots.dad.lib.targetselection;

import robocode.AdvancedRobot;
import smi.robots.dad.lib.intel.Intel;

public class NearestNeighbor extends TargetSelection {
    public NearestNeighbor(AdvancedRobot robot) {
        this(robot, 1.3);
    }
    
    public NearestNeighbor(AdvancedRobot robot, double impedance) {
        super(robot);
        setImpedance(impedance);
    }

    @Override
    public double getEnemyValue(AdvancedRobot robot, Intel data) {
        return robot.getBattleFieldWidth() / data.getDistance();
    }
}
