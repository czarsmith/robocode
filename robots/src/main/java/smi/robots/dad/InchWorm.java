package smi.robots.dad;

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
import smi.robots.dad.lib.firepower.ConstantHitPercent;
import smi.robots.dad.lib.firepower.FirePower;
import smi.robots.dad.lib.guns.CircularGun;
import smi.robots.dad.lib.guns.GunController;
import smi.robots.dad.lib.guns.SimpleGunController;
import smi.robots.dad.lib.intel.RobotManager;
import smi.robots.dad.lib.radar.LockAndSpin;
import smi.robots.dad.lib.radar.Radar;
import smi.robots.dad.lib.targetselection.NearestNeighbor;
import smi.robots.dad.lib.targetselection.TargetSelection;
import smi.robots.dad.lib.wheels.InchWormWheel;
import smi.robots.dad.lib.wheels.SimpleWheelController;
import smi.robots.dad.lib.wheels.WheelController;

public class InchWorm extends AdvancedRobot {
    private WheelController wheelController;
    private GunController gunController;
    private Radar radar;
    private TargetSelection targetSelection;

    @Override
    public void run() {
        setColors(Color.BLACK, Color.YELLOW, Color.BLACK);
        setBulletColor(Color.ORANGE);

        RobotManager.reset();

        FirePower fp = new ConstantHitPercent(this, 1, 4, 3);
        wheelController = new SimpleWheelController(this, new InchWormWheel(this));
        gunController = new SimpleGunController(this, fp, new CircularGun(this, fp));
        radar = new LockAndSpin(this, 45);
        targetSelection = new NearestNeighbor(this, 1.3);

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
