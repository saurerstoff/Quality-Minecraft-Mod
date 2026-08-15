package saurerstoff.quality.criterions;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SmeltQualityCriterion extends SimpleCriterionTrigger<SmeltQualityCriterion.Conditions> {
    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> playerPredicate, int requiredQuality, Optional<ItemStack> requiredItem, int requiredFurnaceQuality)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static Codec<SmeltQualityCriterion.Conditions> CODEC = 
                RecordCodecBuilder.create(instance -> instance.group(
                    ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                    Codec.INT.fieldOf("requiredQuality").forGetter(Conditions::requiredQuality),
                    ItemStack.CODEC.optionalFieldOf("requiredItem").forGetter(Conditions::requiredItem),
                    Codec.INT.fieldOf("requiredFurnaceQuality").forGetter(Conditions::requiredFurnaceQuality)
                ).apply(instance, Conditions::new));

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet(int quality, ItemStack item, int furnaceQuality) {
            if (requiredItem.isPresent() && !requiredItem.get().getItem().equals(item.getItem())) return false;
            return quality >= requiredQuality && furnaceQuality >= requiredFurnaceQuality; // AbstractCriterion#trigger helpfully checks the playerPredicate for us.
        }

    }

    public void trigger(ServerPlayer player, int quality, ItemStack item, int furnaceQuality) {
        trigger(player, conditions -> conditions.requirementsMet(quality, item, furnaceQuality));
    }
}
