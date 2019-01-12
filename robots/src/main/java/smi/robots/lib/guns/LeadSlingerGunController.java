package smi.robots.lib.guns;

import robocode.AdvancedRobot;
import smi.robots.lib.firepower.FirePower;
import smi.robots.lib.guns.guessfactor.GuessFactor;

public class LeadSlingerGunController extends BestGunController {
    public LeadSlingerGunController(AdvancedRobot robot, FirePower firepower) {
        super(robot, firepower,
            new GuessFactor(robot, firepower),
            new HeadOnGun(robot, firepower),
            new CircularGun(robot, firepower));
    }
}
