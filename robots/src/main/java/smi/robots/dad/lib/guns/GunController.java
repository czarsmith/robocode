package smi.robots.dad.lib.guns;

import robocode.AdvancedRobot;
import robocode.Event;
import smi.robots.dad.lib.firepower.FirePower;
import smi.robots.dad.lib.intel.RobotManager;
import smi.robots.dad.lib.intel.SelfIntel;

public class GunController {
    protected AdvancedRobot robot;
    protected FirePower firepower;

    public GunController(AdvancedRobot robot, FirePower firepower) {
        this.robot = robot;
        this.firepower = firepower;
    }

    public void doTurn() {
        firepower.doTurn();

        SelfIntel me = RobotManager.getMyRobot();
        if (robot.getGunHeat() < robot.getGunCoolingRate() &&
            robot.getGunTurnRemainingRadians() == 0 && me.getOthers() > 0) {
            robot.setFire(firepower.getPower());
        }
    }

    public void onEvent(Event e) {
        firepower.onEvent(e);
    }
}
