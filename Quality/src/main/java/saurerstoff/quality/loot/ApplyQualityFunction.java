package saurerstoff.quality.loot;

import java.util.List;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import saurerstoff.quality.QualityData;
import saurerstoff.quality.QualityUtils;
import saurerstoff.quality.QualityData.QualityParticleEntry;
import saurerstoff.quality.QualityData.QualityRollSoundData;
import saurerstoff.quality.enchantment.QualityEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ApplyQualityFunction extends LootItemConditionalFunction {

    public static final MapCodec<ApplyQualityFunction> CODEC = RecordCodecBuilder
            .mapCodec(instance -> commonFields(instance).apply(instance, ApplyQualityFunction::new));

    private ApplyQualityFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ModLootFunctions.APPLY_QUALITY;
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext lootContext) {
        int level = 0;
        ItemStack tool = null;

        if (lootContext.hasParam(LootContextParams.THIS_ENTITY)
                && lootContext.hasParam(LootContextParams.ATTACKING_ENTITY)) {
            // loot comes from killing an entity

            Entity attacker = lootContext.getParam(LootContextParams.ATTACKING_ENTITY);
            if (attacker instanceof Player) {
                tool = lootContext.getParam(LootContextParams.DAMAGE_SOURCE).getWeaponItem();
            } else {
                return itemStack;
            }

        } else {
            tool = lootContext.getParamOrNull(LootContextParams.TOOL);
            if (tool == null || !isLootValidForTool(itemStack, tool, lootContext)) {
                return itemStack;
            }
        }

        // make sure that loot is obtained through a tool
        if (tool == null || tool.isEmpty()) {
            return itemStack;
        }

        level = getQualityEnchantmentLevel(lootContext, tool);

        int quality = QualityUtils.rollAndApplyQuality(itemStack, lootContext, level);

        // play effects
        Vec3 position = lootContext.getParamOrNull(LootContextParams.ORIGIN);
        BlockPos blockPos = BlockPos.containing(position);
        if (blockPos != null && quality > 0) {

            QualityRollSoundData soundData = QualityData.QUALITY_ROLL_SOUNDS[quality];
            lootContext.getLevel().playSound(null, blockPos, soundData.soundEvent(), SoundSource.PLAYERS,
                    soundData.volume(), soundData.pitch());

            List<QualityParticleEntry> particles = QualityData.QUALITY_PARTICLES.get(quality);
            for (QualityParticleEntry particleEntry : particles) {
                lootContext.getLevel().sendParticles(
                        particleEntry.particleOptions(),
                        position.x, position.y, position.z,
                        particleEntry.amount(),
                        0.2, 0.2, 0.2,
                        0.1);
            }

        }

        return itemStack;
    }

    private int getQualityEnchantmentLevel(LootContext lootContext, ItemStack tool) {
        int level;
        var enchantmentRegistry = lootContext.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var qualityEnchantment = enchantmentRegistry.getOrThrow(QualityEnchantments.QUALITY_MINING);
        level = EnchantmentHelper.getItemEnchantmentLevel(qualityEnchantment, tool);
        return level;
    }

    /* 
    private int rollAndApplyQuality(ItemStack itemStack, LootContext lootContext, int level) {
        if (level > 0) {
            RandomSource random = lootContext.getRandom();
            int qualityLevel = 0;

            float q = 0.03f + (level - 1) * 0.06f; 
            float roll = random.nextFloat();
            float chance = 0;
            if (roll <= (chance = q * 1.f / 1000.f))
                qualityLevel = 4;
            else if (roll <= (chance += q * 9.f / 1000.f))
                qualityLevel = 3;
            else if (roll <= (chance += q * 9.f / 100.f))
                qualityLevel = 2;
            else if (roll <= (chance += q * 9.f / 10.f))
                qualityLevel = 1;

            if (qualityLevel > 0 && qualityLevel < 5) {
                itemStack.set(ModComponents.QUALITY_COMPONENT, qualityLevel);
                return qualityLevel;
            }
        }

        return 0;
    }
    */

    private boolean isLootValidForTool(ItemStack lootStack, ItemStack tool, LootContext lootContext) {
        if (tool.is(ItemTags.PICKAXES)) {
            return lootStack.is(Items.COAL)
                    || lootStack.is(Items.RAW_GOLD)
                    || lootStack.is(Items.GOLD_NUGGET)
                    || lootStack.is(Items.RAW_IRON)
                    || lootStack.is(Items.LAPIS_LAZULI)
                    || lootStack.is(Items.RAW_COPPER)
                    || lootStack.is(Items.DIAMOND)
                    || lootStack.is(Items.EMERALD)
                    || lootStack.is(Items.REDSTONE)
                    || lootStack.is(ItemTags.STONE_CRAFTING_MATERIALS)
                    || lootStack.is(Items.OBSIDIAN)
                    || lootStack.is(Items.AMETHYST_SHARD);
        } else if (tool.is(ItemTags.AXES)) {
            return lootStack.is(ItemTags.LOGS);
        } else if (tool.is(ItemTags.SHOVELS)) {
            return lootStack.is(ItemTags.DIRT)
                    || lootStack.is(ItemTags.SAND)
                    || lootStack.is(Items.GRAVEL)
                    || lootStack.is(Items.CLAY)
                    || lootStack.is(Items.FLINT)
                    || lootStack.is(Items.CLAY_BALL);
        } else if (tool.is(ItemTags.SWORDS)) {
            return false; // reality check: swords should never run through this check
        } else {
            return false;
        }
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(ApplyQualityFunction::new);
    }

}
