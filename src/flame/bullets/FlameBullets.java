package flame.bullets;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import flame.special.states.*;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.style.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import flame.*;
import flame.audio.*;
import flame.effects.*;
import flame.entities.*;
import flame.graphics.*;
import flame.special.*;
import flame.unit.empathy.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.mod.*;
import mindustry.mod.Mods.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.fragments.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import java.lang.reflect.*;


public class FlameBullets{
    public static BulletType smallLaser, sweep, aoe, bigLaser, sentryLaser, pin, tracker, sword, test,crushed;

        static class Part2 extends DrawEntity{
        float rotation, bend;
        float time = 0f;

        float targetBend, targetRotation, bendTime;
        float smooth;
        float length = 100f, width = 10f;

        float fric1, fric2;
        boolean flipped = Mathf.chance(0.5f);
        boolean end = Mathf.chance(0.5f);

        float vx, vy, vr;
        float landTime = Mathf.random(20f, 30f);

        @Override
        public void update(){
            time += Time.delta;

            x += vx * Time.delta;
            y += vy * Time.delta;
            rotation += vr * Time.delta;
            targetRotation += vr * Time.delta;

            if(time <= landTime){
                return;
            }else{
                float drag = 1f - Mathf.clamp(0.5f * Time.delta);
                vx *= drag;
                vy *= drag;
                vr *= drag;
            }

            v1.trns(rotation + bend, length).add(x, y);
            float lx1 = v1.x, ly1 = v1.y;
            v1.trns(rotation - bend, length).add(x, y);
            float lx2 = v1.x, ly2 = v1.y;

            float lmx = (lx1 + lx2 + x) / 3f;
            float lmy = (ly1 + ly2 + y) / 3f;

            if((bendTime -= Time.delta) <= 0f){
                targetBend = Mathf.random(25f, 90f) * (flipped ? -1 : 1);
                targetRotation = Mathf.mod(rotation + Mathf.random(5f), 360f);
                bendTime = Mathf.random(15f, 40f);

                fric1 = Mathf.random();
                fric2 = Mathf.random();
                smooth = 0f;
            }

            bend = Mathf.lerpDelta(bend, targetBend, 0.1f * (smooth = Mathf.lerpDelta(smooth, 1f, 0.25f)));
            rotation = Mathf.slerpDelta(rotation, targetRotation, 0.01f);

            v1.trns(rotation + bend, length).add(x, y);
            float nx1 = v1.x, ny1 = v1.y;
            v1.trns(rotation - bend, length).add(x, y);
            float nx2 = v1.x, ny2 = v1.y;

            float nmx = (nx1 + nx2 + x) / 3f;
            float nmy = (ny1 + ny2 + y) / 3f;

            float dx = nmx - lmx;
            float dy = nmy - lmy;

            float total = fric1 + fric2;
            if(total > Mathf.FLOAT_ROUNDING_ERROR){
                float dscl = Math.max(total, 1f);

                v1.trns(rotation + bend, length).add(x - dx, y - dy);
                float ox1 = v1.x, oy1 = v1.y;
                v1.trns(rotation - bend, length).add(x - dx, y - dy);
                float ox2 = v1.x, oy2 = v1.y;

                float mx1 = (ox1 - lx1) * (fric1 / dscl), my1 = (oy1 - ly1) * (fric1 / dscl);
                float mx2 = (ox2 - lx2) * (fric2 / dscl), my2 = (oy2 - ly2) * (fric2 / dscl);

                dx += mx1 + mx2;
                dy += my1 + my2;
            }

            x -= dx;
            y -= dy;

            if(time > 25f * 60){
                remove();
            }
        }

        @Override
        public float clipSize(){
            return length * 2f + 10f;
        }

        @Override
        public void draw(){
            int sign = flipped ? -1 : 1;
            float fout = Mathf.clamp((25f * 60 - time) / 120f);

            Draw.z(Layer.debris + 1.01f);
            Draw.color(Color.white, fout);

            v1.trns(rotation + bend, length).add(x, y);
            float lx1 = v1.x, ly1 = v1.y;
            v1.trns(rotation - bend, length).add(x, y);
            float lx2 = v1.x, ly2 = v1.y;

            TextureRegion branch = SpecialMain.regionSeq.get(10);
            TextureRegion flower = SpecialMain.regionSeq.get(9);

            Lines.stroke(width * sign);
            Lines.line(branch, lx1, ly1, x, y, false);
            Lines.line(branch, x, y, lx2, ly2, false);
            
            if(end) Lines.line(flower, x, y, lx2, ly2, false);

            float jx = (lx2 - x) * 0.25f + x, jy = (ly2 - y) * 0.25f + y;
            float len = length + length * 0.25f;
            float dst = Mathf.dst(lx1, ly1, jx, jy) / len;
            float wscl = 1f + (1f - dst);

            Lines.stroke(width * sign * wscl);
            Lines.line(flower, lx1, ly1, jx, jy, false);
            
            float offwid = width / 3.5f;
            float offLen = length * 0.05f;
            
            //v1.trns((rotation - (bend / 2f)) - 90f * sign, offwid).add(x, y);
            //v1.trns((rotation + 90f - (90f - bend) / 2f) - 90f * sign, offwid).add(x, y);
            //v1.trns((rotation + 45f + (90f - bend) / 2f) - 90f * sign, offwid).add(x, y);
            v1.trns((rotation - bend) - 90f, offwid * sign, offLen).add(x, y);
            float rx = v1.x, ry = v1.y;
            float tlen = length + offLen;
            float len2 = Mathf.sqrt(tlen * tlen + offwid * offwid);
            float dst2 = Mathf.dst(lx1, ly1, rx, ry) / len2;
            float wscl2 = 1f + (1f - dst2);
            
            Lines.stroke(width * -sign * 0.5f * wscl2);
            Lines.line(flower, lx1, ly1, rx, ry, false);
        }
    }

        static class Part extends DrawEntity{
        int textureIdx = Mathf.random(4);
        float vx, vy, vr;
        float rotation;
        float time = 0f;
        float timeOffset = Mathf.random(24f);
        float landTime = Mathf.random(15f, 23f);
        float hitSize = 1f;
        boolean flipped = Mathf.chance(0.5f);
        boolean landed;

        @Override
        public void update(){
            time += Time.delta;
            if(time > landTime && !landed){
                landed = true;
            }

            x += vx * Time.delta;
            y += vy * Time.delta;
            rotation += vr * Time.delta;

            if(textureIdx == 0) rotation += Mathf.range(2.5f);

            //vx *= 1f
            float drag = 1f - Mathf.clamp((landed ? 0.5f : 0.05f) * Time.delta);

            vx *= drag;
            vy *= drag;
            vr *= drag;

            if(time >= 25f * 60){
                remove();
            }
        }

        @Override
        public float clipSize(){
            return 90f;
        }

        @Override
        public void draw(){
            TextureRegion reg = SpecialMain.regionSeq.get(4 + textureIdx);
            float fout = Mathf.clamp((25f * 60 - time) / 120f);
            float scl = 1;

            if(textureIdx == 3){
                //scl = (1f - (((time + timeOffset) / 24f) % 1f)) * 0.125f + (1f - 0.125f);
                scl = 1f - (1f - (((time + timeOffset) / 24f) % 1f)) * 0.125f;
            }
            if(textureIdx == 4){
                scl = Mathf.absin((time + timeOffset), 40f, 0.1f) + 0.9f;
            }

            Draw.z(Layer.debris + 1f);
            Draw.color(Color.white, fout);
            Draw.rect(reg, x, y, reg.width * Draw.scl * hitSize * (flipped ? -1f : 1f) * scl, reg.height * Draw.scl * hitSize * scl, rotation);
        }
    }

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
            EmpathyDamage.damageUnit(unit, unit.maxHealth + 900000f, true, () -> {
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
                    };            
                                                      };
    }
