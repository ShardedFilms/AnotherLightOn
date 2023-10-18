package flame.unit;

import flame.unit.shifts.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.meta.*;

public class FlameUnitTypes{
    public static UnitType apathy, empathy;

    public static void load(){
        apathy = new ApathyUnitType("apathy"){{
            flying = true;
            hitSize = 75;
            drag = 0.06f;
            //playerControllable = logicControllable = false;
            
            handlers.add(new PrismShift("apathy"));
            handlers.add(new WeakLaserShift("apathy-weak-laser"));
            handlers.add(new AoEShift("apathy-aoe"));
            handlers.add(new SweepShift("apathy-sweep"));
            handlers.add(new StrongLaserShift("apathy-strong-laser"));

            controller = unit -> new ApathyIAI();

            fallEffect = fallEngineEffect = Fx.none;
            fallSpeed = 0f;
            deathExplosionEffect = Fx.none;
            createScorch = false;
            
            envEnabled = Env.any;
            envDisabled = 0;
        }};
        empathy = new EmpathyUnitType("empathy");
    }
}
