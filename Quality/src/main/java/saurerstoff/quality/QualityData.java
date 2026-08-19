package saurerstoff.quality;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class QualityData {
    public record QualityRollSoundData(SoundEvent soundEvent, float volume, float pitch) {
    }

    public record QualityParticleEntry(ParticleOptions particleOptions, int amount) {
    }

    public final static Float[] MINING_SPEED_BONUS = { 1.f, 1.25f, 1.6f, 2.2f, 3.0f };
    public final static Float[] FURNACE_COOKTIME_BONUS = { 1.f, 0.9f, 0.79f, 0.66f, 0.5f };
    public final static Float[] SWORD_DAMAGE_BONUS = { 0.f, 0.5f, 1f, 2f, 4f };
    public final static Float[] ARMOR_BONUS = { 0.f, 0.5f, 1f, 1.75f, 3f };
    public final static Integer[] FISHING_LUCK_BONUS = { 0, 1, 2, 3, 5 };
    public final static Integer[] LAPIS_ENCHANT_BONUS = { 0, 3, 7, 15, 30 };
    public final static Float[] FURNACE_FUEL_BONUS = { 1.f, 1.25f, 1.6f, 2.2f, 3.0f };
    public final static Float[] SPYGLASS_ZOOM_BONUS = { 1.f, 1.4f, 2f, 3f, 5f };
    public final static QualityRollSoundData[] QUALITY_ROLL_SOUNDS = {
            new QualityRollSoundData(SoundEvents.BAMBOO_WOOD_HIT, 0f, 1f),
            new QualityRollSoundData(SoundEvents.NOTE_BLOCK_BELL.value(), 0.05f, 0.5f),
            new QualityRollSoundData(SoundEvents.NOTE_BLOCK_BELL.value(), 0.3f, 0.75f),
            new QualityRollSoundData(SoundEvents.NOTE_BLOCK_BELL.value(), 0.6f, 1f),
            new QualityRollSoundData(SoundEvents.NOTE_BLOCK_CHIME.value(), 1f, 0.75f),
    };
    public final static List<List<QualityParticleEntry>> QUALITY_PARTICLES = List.of(
            List.of(),
            List.of(
                new QualityParticleEntry(new DustParticleOptions(new Vector3f(0, 1, 0), 0.5f), 5)
            ),
            List.of(
                new QualityParticleEntry(new DustParticleOptions(new Vector3f(0.4f, 0.4f, 1), 0.9f), 10)
            ),
            List.of(
                new QualityParticleEntry(new DustParticleOptions(new Vector3f(0.741f, 0.149f, 0.714f), 1f), 20),
                new QualityParticleEntry(ParticleTypes.POOF, 4)
            ),
            List.of(
                new QualityParticleEntry(new DustParticleOptions(new Vector3f(0.788f, 0.753f, 0), 1f), 20),
                new QualityParticleEntry(ParticleTypes.POOF, 5),
                new QualityParticleEntry(ParticleTypes.ENCHANT, 30)
            )
        );

}

/*
 * TODO:
 * 
 * - BOAT
 * - TRIDENT
 * - MACE
 * - BOW
 * - CROSSBOW
 * - TOTEM OF UNDYING
 * - WIND CHARGE
 * - ARROW
 * - CAMPFIRE / SOUL CAMPFIRE
 * - BREWING STAND
 * - POTIONS
 * - FOOD
 * - BRUSH
 * - CHEST / BARREL / ENDERCHEST / SHULKER BOXES
 * - EMERALDS (VILLAGER TRADING)
 * 
 */