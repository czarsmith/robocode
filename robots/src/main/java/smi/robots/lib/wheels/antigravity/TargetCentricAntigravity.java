package smi.robots.lib.wheels.antigravity;

import robocode.AdvancedRobot;
import smi.robots.lib.intel.Intel;
import smi.robots.lib.intel.RobotManager;

public class TargetCentricAntigravity extends CenterPhobia {
    public static final double TARGET_MAGNITUDE = DEFAULT_MAGNITUDE;
    public static final double TARGET_DECAY = 1.7;
    public static final double TARGET_RANGE = 350;

    private double targetMagnitude;
    private double targetDecay;
    private double targetRange;

    public TargetCentricAntigravity(AdvancedRobot robot) {
        this(robot, TARGET_MAGNITUDE, TARGET_DECAY, TARGET_RANGE);
    }

    public TargetCentricAntigravity(AdvancedRobot robot, double targetMagnitude,
        double targetDecay, double targetRange) {
        super(robot);

        this.targetMagnitude = targetMagnitude;
        this.targetDecay = targetDecay;
        this.targetRange = targetRange;
    }

    @Override
    protected double getMagnitude(Intel intel) {
        return RobotManager.isTarget(intel) ? targetMagnitude : super.getMagnitude(intel);
    }
    
    /**
     * Don't let non-target enemies attract the robot.
     */
    @Override
    protected double getOptimumRange(Intel intel) {
        return RobotManager.isTarget(intel) ? targetRange : Double.POSITIVE_INFINITY;
    }
    
    @Override
    protected double getDecay(Intel intel) {
        return RobotManager.isTarget(intel) ? targetDecay : super.getDecay(intel);
    }
}
