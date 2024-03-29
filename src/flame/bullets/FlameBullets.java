package flame.bullets;

import arc.graphics.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import flame.special.states.*;

public class FlameBullets{
    public static BulletType smallLaser, sweep, aoe, bigLaser, sentryLaser, pin, tracker, sword, test,crushed;

    public static void load(){
        smallLaser = new ApathySmallLaserBulletType();
        sweep = new ApathySweepLaserBulletType();
        aoe = new ApathyAoEBulletType();
        bigLaser = new ApathyBigLaserBulletType();

        sentryLaser = new LaserBulletType(900f){{
            length = 1400f;
            colors = new Color[]{Color.white};
            width = 5f;
        }};

        pin = new EmpathyPinBulletType();
        tracker = new EmpathyTrackerBulletType();
        sword = new EmpathySwordBulletType();

        test = new TestBulletType();
                        crushed = new RailBulletType(){{
                    shootEffect = Fx.railShoot;
                    length = 2400;
                    pointEffectSpace = 60f;
                    pierceEffect = Fx.railHit;
                    pointEffect = Fx.railTrail;
                    hitEffect = Fx.massiveExplosion;
                    smokeEffect = Fx.shootBig2;
                    damage = 1250;
                    pierceDamageFactor = 0.5f;
                }
                    public void hitEntity(Bullet b, Hitboxc entity, float health) {
                        super.hitEntity(b,entity,health);

                        if(entity instanceof Unit unit){
                            if(impact) 
                            Stage2.killUnit(u);
                        };
                    }            
                                                      };
    }
}
