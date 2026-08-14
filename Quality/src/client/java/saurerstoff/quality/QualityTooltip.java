package saurerstoff.quality;

import java.util.function.Function;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

public class QualityTooltip {

    public static void registerTooltips() {
        registerQualityDisplay();
        registerQualityBuffTooltips();
        
    }

    private static void registerQualityBuffTooltips() {
        //swords
        registerQualityBuffTooltip(
            stack -> {
                return stack.is(ItemTags.SWORDS);
            }, 
            QualityData.SWORD_DAMAGE_BONUS, 
            bonus -> {
                return "+" + bonus + " Attack Damage";
            }
        );

        //tools
        registerQualityBuffTooltip(
            stack -> {
                return stack.has(DataComponents.TOOL) && !stack.is(ItemTags.SWORDS);
            }, 
            QualityData.MINING_SPEED_BONUS, 
            bonus -> {
                return "+" + (int)((bonus - 1f) * 100f) + "% Mining Speed";
            }
        );

        //furnace
        registerQualityBuffTooltip(
            stack -> {
                return (stack.is(Items.FURNACE) || stack.is(Items.BLAST_FURNACE) || stack.is(Items.SMOKER));
            }, 
            QualityData.FURNACE_COOKTIME_BONUS, 
            bonus -> {
                return "-" + (int)((1f - bonus) * 100f) + "% Smelting/Cooking Duration";
            }
        );

        //armor
        registerQualityBuffTooltip(
            stack -> {
                return stack.is(ItemTags.ARMOR_ENCHANTABLE);
            }, 
            QualityData.ARMOR_BONUS, 
            bonus -> {
                return "+" + bonus + " Armor";
            }
        );

        //fishing rod
        registerQualityBuffTooltip(
            stack -> {
                return stack.is(Items.FISHING_ROD);
            }, 
            QualityData.FISHING_LUCK_BONUS, 
            bonus -> {
                return "+" + bonus + " Luck";
            }
        );

        //lapis
        registerQualityBuffTooltip(
            stack -> {
                return stack.is(Items.LAPIS_LAZULI);
            },
            QualityData.LAPIS_ENCHANT_BONUS,
            bonus -> {
                return "+" + bonus + " Enchant Power";
            }
        );

        //furnace fuel
        registerQualityBuffTooltip(
            stack -> {
                return AbstractFurnaceBlockEntity.isFuel(stack);
            },
            QualityData.FURNACE_FUEL_BONUS,
            bonus -> {
                return "+" + (int)((bonus - 1f) * 100f) + "% Burn Duration";
            }
        );
    }

    private static <T> void registerQualityBuffTooltip(Function<ItemStack, Boolean> isTarget, T[] bonusValues, Function<T, String> displayBonus ) {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.has(ModComponents.QUALITY_COMPONENT) && isTarget.apply(stack)) {
                Integer quality = stack.get(ModComponents.QUALITY_COMPONENT);
                T bonus = bonusValues[quality];
                lines.add(Component.literal(displayBonus.apply(bonus))
                        .withStyle(ChatFormatting.YELLOW));
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
