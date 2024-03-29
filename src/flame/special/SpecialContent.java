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
        

    class SpawnerBuilding extends Building{
        Seq<UnitType> types = new Seq<>();
        static Seq<UnitType> tmp = new Seq<>();
        static int rowCount;

        UnitType currentUnit;

        @Override
        public void updateProximity(){
            updateUnits();
        }

        void updateUnits(){
            LoadedMod minfo = null;
            tmp.clear();

            if(types.isEmpty()){
                for(UnitType unit : Vars.content.units()){
                    if(unit.internal || unit.hidden) continue;
                    
                    if(unit.minfo != null && unit.minfo.mod != minfo){
                        tmp.sort(u -> -(FlameOutSFX.inst.getUnitDps(u) + u.health));
                        types.addAll(tmp, 0, Math.min(10, tmp.size));
                        tmp.clear();

                        minfo = unit.minfo.mod;
                    }

                    if(/*unit.hitSize >= 45 || unit.legLength > 60f || unit.health > 2000000f*/true) tmp.add(unit);
                }
            }
            if(!tmp.isEmpty()){
                tmp.sort(u -> -(FlameOutSFX.inst.getUnitDps(u) + u.health));
                types.addAll(tmp, 0, Math.min(10, tmp.size));
                tmp.clear();
            }
        }

        @Override
        public void updateTableAlign(Table table){
            //const pos = Core.input.mouseScreen(this.x, this.y - Vars.tilesize / 2 - 1);
            Vec2 pos = Core.input.mouseScreen(x, y - Vars.tilesize / 2f - 1);
            table.setPosition(pos.x, pos.y, Align.top);
        }

        @Override
        public void buildConfiguration(Table table){
            table.button(Icon.add, Styles.cleari, () -> {
                //Log.info("unit spawn: " + currentUnit);
                if(currentUnit != null){
                    currentUnit.spawn(Vars.player.team(), x, y);
                    Vars.control.input.config.hideConfig();
                }
            }).width(40f * 5).height(40f);
            table.row();

            ButtonGroup<ImageButton> group = new ButtonGroup<>();
            group.setMinCheckCount(0);
            Table cont = new Table().top();
            cont.defaults().size(40);

            Runnable rebuild = () -> {
                group.clear();
                cont.clearChildren();

                int i = 0;
                rowCount = 0;

                for(UnitType unit : types){
                    ImageButton button = cont.button(Tex.whiteui, Styles.clearTogglei, Mathf.clamp(unit.selectionSize, 0f, 40f), () -> {
                    }).tooltip(unit.localizedName).group(group).get();

                    button.changed(() -> {
                        if(button.isChecked()) currentUnit = unit;
                    });
                    button.getStyle().imageUp = new TextureRegionDrawable(unit.uiIcon);
                    button.update(() -> button.setChecked(currentUnit == unit));

                    /*
                    if(i++ % columns == (columns - 1)){
                        cont.row();
                        rowCount++;
                    }
                    */
                    if(i++ % 8 == 7){
                        cont.row();
                        rowCount++;
                    }
                }
            };
            rebuild.run();

            ScrollPane pane = new ScrollPane(cont, Styles.smallPane);
            pane.setScrollingDisabled(true, false);

            pane.setOverscroll(false, false);
            table.add(pane).maxHeight(40 * 5);
            //table.top().add(table);
        };
    };
    }
}
