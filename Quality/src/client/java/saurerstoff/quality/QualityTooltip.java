package saurerstoff.quality;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

public class QualityTooltip {

    public static void registerTooltips() {
        registerQualityDisplay();
        registerQualityBuffTooltips();
    }

    private static void registerQualityBuffTooltips(){
        registerToolBuffTooltip();
        registerFurnaceBuffTooltip();
        registerSwordBuffTooltip();
    }

    private static void registerSwordBuffTooltip() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.has(ModComponents.QUALITY_COMPONENT) && (stack.is(ItemTags.SWORDS))) {
                Integer quality = stack.get(ModComponents.QUALITY_COMPONENT);
                float bonus = QualityData.SWORD_DAMAGE_BONUS[quality];
                lines.add(Component.literal("+" +  bonus + " Attack Damage")
                    .withStyle(ChatFormatting.YELLOW));
            }
        });
    }

    private static void registerFurnaceBuffTooltip() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.has(ModComponents.QUALITY_COMPONENT) && 
            (stack.is(Items.FURNACE) || stack.is(Items.BLAST_FURNACE) || stack.is(Items.SMOKER))) {
                Integer quality = stack.get(ModComponents.QUALITY_COMPONENT);
                int bonus = 0;
                switch (quality) {
                    case 1:
                        bonus = (int)((1.f - QualityData.UNCOMMON_FURNACE_COOKTIME_MOD) * 100);
                        break;
                    case 2:
                        bonus = (int)((1.f - QualityData.RARE_FURNACE_COOKTIME_MOD) * 100);
                        break;
                    case 3:
                        bonus = (int)((1.f - QualityData.EPIC_FURNACE_COOKTIME_MOD) * 100);
                        break;
                    case 4:
                        bonus = (int)((1.f - QualityData.LEGENDARY_FURNACE_COOKTIME_MOD) * 100);
                        break;
                    default:
                        break;
                }
                lines.add(Component.literal("-" +  bonus + "% Smelting/Cooking Duration")
                    .withStyle(ChatFormatting.YELLOW));
            }
        });
    }

    private static void registerToolBuffTooltip(){
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.has(ModComponents.QUALITY_COMPONENT) && stack.has(DataComponents.TOOL) && !stack.is(ItemTags.SWORDS)) {
                Integer quality = stack.get(ModComponents.QUALITY_COMPONENT);
                int bonus = 0;
                switch (quality) {
                    case 1:
                        bonus = (int)((QualityData.UNCOMMON_MINING_SPEED_MOD - 1.f) * 100);
                        lines.add(Component.literal("+" +  bonus + "% Mining Speed")
                                .withStyle(ChatFormatting.YELLOW));
                        break;
                    case 2:
                        bonus = (int)((QualityData.RARE_MINING_SPEED_MOD - 1.f) * 100);
                        lines.add(Component.literal("+" +  bonus + "% Mining Speed")
                                .withStyle(ChatFormatting.YELLOW));
                        break;
                    case 3:
                        bonus = (int)((QualityData.EPIC_MINING_SPEED_MOD - 1.f) * 100);
                        lines.add(Component.literal("+" +  bonus + "% Mining Speed")
                                .withStyle(ChatFormatting.YELLOW));
                        break;
                    case 4:
                        bonus = (int)((QualityData.LEGENDARY_MINING_SPEED_MOD - 1.f) * 100);
                        lines.add(Component.literal("+" +  bonus + "% Mining Speed")
                                .withStyle(ChatFormatting.YELLOW));
                        break;
                    default:
                        break;
                }
            }
        });
    }
    

    private static void registerQualityDisplay() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.has(ModComponents.QUALITY_COMPONENT)) {
                Integer quality = stack.get(ModComponents.QUALITY_COMPONENT);
                switch (quality) {
                    case 1:
                        lines.add(Component.literal("Uncommon")
                                .withStyle(ChatFormatting.GREEN));
                        break;
                    case 2:
                        lines.add(Component.literal("Rare")
                                .withStyle(ChatFormatting.BLUE));
                        break;
                    case 3:
                        lines.add(Component.literal("Epic")
                                .withStyle(ChatFormatting.DARK_PURPLE));
                        break;
                    case 4:
                        lines.add(Component.literal("Legendary")
                                .withStyle(ChatFormatting.GOLD));
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
