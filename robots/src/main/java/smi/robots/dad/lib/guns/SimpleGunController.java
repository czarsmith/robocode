package smi.robots.dad.lib.guns;

import robocode.AdvancedRobot;
import robocode.Event;
import robocode.util.Utils;
import smi.robots.dad.lib.MyUtils;
import smi.robots.dad.lib.firepower.FirePower;
import smi.robots.dad.lib.intel.RobotManager;

public class SimpleGunController extends GunController {
    private Gun gun;

    public SimpleGunController(AdvancedRobot robot, FirePower firepower, Gun gun) {
        super(robot, firepower);
        this.gun = gun;
    }

    @Override
    public void doTurn() {
        super.doTurn();

        if (RobotManager.getTarget() != null) {
            VirtualBullet bullet = gun.fire(RobotManager.getTarget(), firepower.getPower());
            robot.setTurnGunLeftRadians(Utils.normalRelativeAngle(
                robot.getGunHeadingRadians() - bullet.getHeading()));

            // Draw the target on the screen
            MyUtils.drawBullet(robot, bullet, gun.getColor());
        }        
    }

    public void onEvent(Event e) {
        super.onEvent(e);
        gun.onEvent(e);
    }
}
