package smi.robots;

import java.awt.Color;

import robocode.AdvancedRobot;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import smi.robots.lib.firepower.ConstantHitPercent;
import smi.robots.lib.firepower.FirePower;
import smi.robots.lib.guns.GunController;
import smi.robots.lib.guns.HeadOnGun;
import smi.robots.lib.guns.SimpleGunController;
import smi.robots.lib.intel.RobotManager;
import smi.robots.lib.radar.Radar;
import smi.robots.lib.radar.WideLock;
import smi.robots.lib.targetselection.NearestNeighbor;
import smi.robots.lib.targetselection.TargetSelection;
import smi.robots.lib.wheels.SimpleWheelController;
import smi.robots.lib.wheels.SpinWheel;
import smi.robots.lib.wheels.WheelController;

public class Marie extends AdvancedRobot {
    private WheelController wheelController;
    private GunController gunController;
    private Radar radar;
    private TargetSelection targetSelection;

    @Override
    public void run() {
        setColors(Color.PINK, Color.PINK, Color.PINK);
        setBulletColor(Color.ORANGE);

        RobotManager.reset();

        wheelController = new SimpleWheelController(this, new SpinWheel(this));
        FirePower fp = new ConstantHitPercent(this, 1, 4, 3);
        gunController = new SimpleGunController(this, fp, new HeadOnGun(this, fp));
        radar = new WideLock(this);
        targetSelection = new NearestNeighbor(this);

        while(true) {
            RobotManager.doTurn(this);
            targetSelection.doTurn();
            wheelController.doTurn();
            radar.doTurn();
            gunController.doTurn();
            execute();
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        RobotManager.onScannedRobot(this, e);
        targetSelection.onEvent(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        RobotManager.onRobotDeath(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
        targetSelection.onEvent(e);
    }

    @Override
    public void onDeath(DeathEvent e) {
        targetSelection.onEvent(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
    }

    public void onHitRobot(HitRobotEvent e) {
        targetSelection.onEvent(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
    }
    
    public void onBulletHit(BulletHitEvent e) {
        targetSelection.onEvent(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
    }

    public void onBulletHitBullet(BulletHitBulletEvent e) {
        targetSelection.onEvent(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
    }
    
    public void onBulletMissed(BulletMissedEvent e) {
        targetSelection.onEvent(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
    }
    
    public void onHitByBullet(HitByBulletEvent e) {
        targetSelection.onEvent(e);
        wheelController.onEvent(e);
        radar.onEvent(e);
        gunController.onEvent(e);
    }
}
