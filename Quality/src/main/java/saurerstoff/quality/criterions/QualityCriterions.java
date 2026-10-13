package saurerstoff.quality.criterions;

import net.minecraft.advancements.CriteriaTriggers;
import saurerstoff.quality.Quality;

public class QualityCriterions {
    public static final CraftQualityCriterion CRAFT_QUALITY = CriteriaTriggers.register(Quality.MOD_ID + ":craft_quality", new CraftQualityCriterion());
    public static final SmeltQualityCriterion SMELT_QUALITY = CriteriaTriggers.register(Quality.MOD_ID + ":smelt_quality", new SmeltQualityCriterion());
    public static final EnchantQualityCriterion ENCHANT_QUALITY = CriteriaTriggers.register(Quality.MOD_ID + ":enchant_quality", new EnchantQualityCriterion());

    public static void initialize(){

    }
}
