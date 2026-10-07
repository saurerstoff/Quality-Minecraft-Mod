package saurerstoff.quality.blocks;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import saurerstoff.quality.Quality;

public class ModBlockEntities {
    public static final BlockEntityType<RecyclerBlockEntity> RECYCLER_BLOCK_ENTITY 
        = register("recycler", RecyclerBlockEntity::new, ModBlocks.RECYCLER);

    private static <T extends BlockEntity> BlockEntityType<T> register(String name,
            BlockEntityType.BlockEntitySupplier<? extends T> entityFactory,
            Block... blocks) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id,
                BlockEntityType.Builder.<T>of(entityFactory, blocks).build());
    }

    public static void initialize() {}
}
