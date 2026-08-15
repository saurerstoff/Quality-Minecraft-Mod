package saurerstoff.quality.criterions;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CraftQualityCriterion extends SimpleCriterionTrigger<CraftQualityCriterion.Conditions> {
    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> playerPredicate, int requiredQuality, List<ItemPredicate> items)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static Codec<CraftQualityCriterion.Conditions> CODEC = 
                RecordCodecBuilder.create(instance -> instance.group(
                    ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                    Codec.INT.fieldOf("requiredQuality").forGetter(Conditions::requiredQuality),
                    ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(Conditions::items)
                ).apply(instance, Conditions::new));

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet(int quality, ItemStack item) {
            if (!this.items.isEmpty()){
                for (ItemPredicate itemPredicate : items) {
                    if (!itemPredicate.test(item)) return false;
                }
            }
            return quality >= requiredQuality; // AbstractCriterion#trigger helpfully checks the playerPredicate for us.
        }

    }

    public void trigger(ServerPlayer player, int quality, ItemStack item) {
        trigger(player, conditions -> conditions.requirementsMet(quality, item));
    }
}
