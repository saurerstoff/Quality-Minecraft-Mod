package saurerstoff.quality.criterions;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class EnchantQualityCriterion extends SimpleCriterionTrigger<EnchantQualityCriterion.Conditions> {
    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<ContextAwarePredicate> playerPredicate, int requiredQuality)
            implements EnchantQualityCriterion.SimpleInstance {

        public static Codec<EnchantQualityCriterion.Conditions> CODEC = 
                RecordCodecBuilder.create(instance -> instance.group(
                    ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                    Codec.INT.fieldOf("requiredQuality").forGetter(Conditions::requiredQuality)
                ).apply(instance, Conditions::new));

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet(int quality) {
            return quality >= requiredQuality; // AbstractCriterion#trigger helpfully checks the playerPredicate for us.
        }

    }

    public void trigger(ServerPlayer player, int quality) {
        trigger(player, conditions -> conditions.requirementsMet(quality));
    }

}
