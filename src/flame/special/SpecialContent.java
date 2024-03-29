package flame.special;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import flame.*;
import flame.unit.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.mod.Mods.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class SpecialContent{
    static UnitType y;
    public static Block spawner;

    public static void load(){
        if(SpecialMain.main == null) return;

            spawner = new Block("unitspawn"){{
                health = 100000;
                size = 1;
                breakable = true;
                solid = false;

                destructible = true;
                configurable = true;
                
                buildVisibility = BuildVisibility.sandboxOnly;
                category = Category.effect;

                buildType = SpawnerBuilding::new;
            }

                @Override
                public void load(){
                    super.load();

                    region = Blocks.switchBlock.region;
                }

                @Override
                public boolean canBreak(Tile tile){
                    return true;
                }
            };
        
    }
}
