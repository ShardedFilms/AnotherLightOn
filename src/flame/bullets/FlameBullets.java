package flame.bullets;

import arc.graphics.*;
import mindustry.content.*;
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
            EmpathyDamage.damageUnit(u, unit.maxHealth + 900000f, true, () -> {
            float trueSize = Math.max(unit.hitSize, Math.min(unit.type.region.width * Draw.scl, unit.type.region.height * Draw.scl));

            BloodSplatter.setLifetime(45f * 60);
            BloodSplatter.explosion(40, unit.x, unit.y, trueSize / 2f, trueSize * 1.5f + 60f, trueSize * 0.4f / 4f + 12f);
            BloodSplatter.explosion(60, unit.x, unit.y, trueSize / 2f, trueSize + 50f, trueSize * 0.75f / 4f + 25f);
            BloodSplatter.setLifetime();

            FragmentationBatch batch = FlameOut.fragBatch;
            batch.baseElevation = 0f;
            batch.fragFunc = e -> {
                float dx = (e.x - unit.x) / 35f;
                float dy = (e.y - unit.y) / 35f;

                e.vx = dx;
                e.vy = dy;
                e.vr = Mathf.range(2f);
                //e.lifetime = 180f;
                e.vz = Mathf.random(-0.01f, 0.1f);

                e.lifetime = 25f * 60f;
            };
            batch.fragDataFunc = f -> {
                f.fadeOut = true;
                f.trailEffect = Fx.none;
            };
            batch.onDeathFunc = null;
            batch.altFunc = (x, y, r) -> {};
            batch.trailEffect = batch.explosionEffect = null;
            batch.fragColor = Color.white;

            batch.switchBatch(u::draw);

            float size = (trueSize / 2f) / 20f;
            float size2 = (trueSize / 2f);

            int amount = 3 + (int)(size * size);
            int amount2 = Mathf.random(3, 6);
            for(int i = 0; i < amount2; i++){
                Vec2 v = Tmp.v1.trns(Mathf.random(360f), Mathf.random());

                Part2 p = new Part2();
                p.x = unit.x + v.x * (size2 / 3f);
                p.y = unit.y + v.y * (size2 / 3f);
                p.rotation = p.targetRotation = Mathf.random(360f);
                p.bend = p.targetBend = Mathf.random(25f, 90f);

                p.vx = v.x * size2 / 10f;
                p.vy = v.y * size2 / 10f;
                p.vr = Mathf.range(3f);

                p.length = size2 * Mathf.random(0.7f, 1.1f);
                p.width = (p.length / 4f) * Mathf.random(1f, 1.5f);

                p.add();
            }

            for(int i = 0; i < amount; i++){
                Vec2 v = Tmp.v1.trns(Mathf.random(360f), Mathf.random());

                Part p = new Part();
                p.x = unit.x + v.x * size2;
                p.y = unit.y + v.y * size2;
                p.rotation = Mathf.random(360f);

                p.vx = v.x * size2 / 4.5f;
                p.vy = v.y * size2 / 4.5f;
                p.vr = Mathf.range(6f);
                p.hitSize = (size2 / 65f) * Mathf.random(0.9f, 1f);

                p.add();
            }

            Part cage = new Part();
            cage.x = unit.x;
            cage.y = unit.y;
            cage.rotation = Mathf.random(360f);

            Vec2 v = Tmp.v1.trns(Mathf.random(360f), Mathf.random());

            cage.vx += v.x * size2 / 8f;
            cage.vy += v.y * size2 / 8f;
            cage.vr = Mathf.range(6f);
            cage.hitSize = (size2 / 60f) * Mathf.random(0.9f, 1f);

            cage.textureIdx = -1;

            cage.add();
        });
    }
                        };
                    }            
                                                      };
    }
}
