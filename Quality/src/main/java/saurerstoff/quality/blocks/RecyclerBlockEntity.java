package saurerstoff.quality.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.ModSounds;
import saurerstoff.quality.Quality;
import saurerstoff.quality.QualityData;
import saurerstoff.quality.QualityUtils;

public class RecyclerBlockEntity extends BlockEntity implements ImplementedContainer, WorldlyContainer, MenuProvider {
    public static final int CONTAINER_SIZE = 11;
    public static final int DATA_COUNT = 2;
    public static final int RECYCLE_TIME_PER_ITEM_DEFAULT = 15;
    public static final int DATA_RECYCLE_PROGRESS = 0;
    public static final int DATA_RECYCLE_TOTAL_TIME = 1;
    int soundTime = 0; 
    int recycleProgress;
    int recycleTotalTime = RECYCLE_TIME_PER_ITEM_DEFAULT;
    public static final Map<Item, ItemStack[]> outputCache = new HashMap<>();

    // the first slot is the input slot, the rest are output slots
    private final NonNullList<ItemStack> items = NonNullList.withSize(11, ItemStack.EMPTY);

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return RecyclerBlockEntity.this.recycleProgress;
                case 1:
                    return RecyclerBlockEntity.this.recycleTotalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    RecyclerBlockEntity.this.recycleProgress = value;
                    break;
                case 1:
                    RecyclerBlockEntity.this.recycleTotalTime = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public RecyclerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RECYCLER_BLOCK_ENTITY, pos, state);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, Provider provider) {
        super.loadAdditional(compoundTag, provider);
        ContainerHelper.loadAllItems(compoundTag, items, provider);
        this.recycleProgress = compoundTag.getShort("RecycleProgress");
        this.recycleTotalTime = compoundTag.getShort("RecycleTotalTime");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, Provider provider) {
        ContainerHelper.saveAllItems(compoundTag, items, provider);
        super.saveAdditional(compoundTag, provider);
        compoundTag.putShort("RecycleProgress", (short) this.recycleProgress);
        compoundTag.putShort("RecycleTotalTime", (short) this.recycleTotalTime);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP)
            return new int[] { 0 };
        if (side == Direction.DOWN)
            return new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        else
            return new int[] {};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.quality.recycler");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new RecyclerMenu(containerId, inventory, this, dataAccess);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RecyclerBlockEntity entity) {
        entity.soundTime--;
        if (canRecycle(level, entity.items)) { //check if recycling is possible
            playRecycleSoundLoop(entity);
            updateRecycleTime(level, entity);
            entity.recycleProgress++;

            if (entity.recycleProgress >= entity.recycleTotalTime) { //check if recycling is finished
                recycle(level, entity.items, blockPos);
                entity.recycleProgress = 0;
                if(!level.isClientSide()){
                    level.playSound(
                        null,
                        entity.getBlockPos(),
                        ModSounds.RECYCLER_FINISHED,
                        SoundSource.BLOCKS,
                        0.5f, 1f
                    );
                }
            }
        } else {
            entity.recycleProgress = 0;
        }
    }

    //very simple function, that loops the recycle sound, should be replaced in the future by a better system.
    private static void playRecycleSoundLoop(RecyclerBlockEntity entity){
        if(!entity.getLevel().isClientSide() && entity.soundTime <= 0){
            entity.soundTime = ModSounds.RECYCLER_LOOP_DURATION_TICKS;
            entity.getLevel().playSound(
                null, 
                entity.getBlockPos(),
                ModSounds.RECYCLER_LOOP,
                SoundSource.BLOCKS,
                0.1f, 1f
            );
        }
    }

    private static void updateRecycleTime(Level level, RecyclerBlockEntity entity){
        ItemStack input = entity.items.get(0);
        if (input.isEmpty())
            return;

        List<ItemStack> output = getRecycleResult(level, input);
        if (output.isEmpty())
            return;
        else{
            entity.recycleTotalTime = getItemCountFromList(output) * RECYCLE_TIME_PER_ITEM_DEFAULT;
        }
    }

    private static int getItemCountFromList(List<ItemStack> items){
        int amount = 0;
        for (ItemStack itemStack : items) {
            amount += itemStack.getCount();
        }
        return amount;
    }

    private static boolean canRecycle(Level level, NonNullList<ItemStack> items) {
        ItemStack input = items.get(0);
        if (input.isEmpty())
            return false;

        List<ItemStack> output = getRecycleResult(level, input);
        if (output.isEmpty())
            return false;

        return true;
    }

    private static void recycle(Level level, NonNullList<ItemStack> items, BlockPos blockPos) {
        ItemStack input = items.get(0);
        int quality = QualityUtils.getResultQuality(input);
        ArrayList<ItemStack> output = new ArrayList<>();
        RandomSource random = level.getRandom();

        //copy itemstack, because we don't want to alter the cached stacks
        List<ItemStack> baseRecycleOutput = getRecycleResult(level, input);
        for (ItemStack itemStack : baseRecycleOutput) {
            output.add(itemStack.copy());
        }

        // remove output items based on chance
        Iterator<ItemStack> it = output.iterator();
        while (it.hasNext()) {
            ItemStack itemstack = it.next();
            for (int i = 0; i < itemstack.getCount(); i++) {
                if (random.nextFloat() > QualityData.RECYCLE_RETURN_BASE_CHANCE) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty())
                        it.remove();
                }
            }
        }

        // increase quality based on chance
        ArrayList<ItemStack> addedItemStacks = new ArrayList<>();
        it = output.iterator();
        while (it.hasNext()) {
            ItemStack itemstack = it.next();
            int itemCount = itemstack.getCount();
            Quality.LOGGER.info("itemstack " + itemstack.getDisplayName().getString() + ": itemcount " + itemCount);
            for (int i = 0; i < itemCount; i++) { 
                int newQuality = QualityUtils.rollQuality(itemstack, random, 3, quality);

                if (newQuality > 0) {
                    ItemStack qualityItemStack = itemstack.copyWithCount(1); 
                    qualityItemStack.set(ModComponents.QUALITY_COMPONENT, newQuality); 
                    addedItemStacks.add(qualityItemStack); 
                    itemstack.shrink(1); 
                    if (itemstack.isEmpty()){ //remove original stack if stack is empty
                        Quality.LOGGER.info("Itemstack empty, deleting stack");
                        it.remove();
                        break;
                    }
                }
            }
        }
        output.addAll(addedItemStacks);
        Quality.LOGGER.info("output size " + output.size());

        // add output items into output slots
        for (ItemStack outputItem : output) {
            Optional<Integer> outputIndex = getValidItemStack(items, outputItem);
            if (outputIndex.isEmpty())
                outputIndex = getEmptyItemStack(items);
            if (outputIndex.isEmpty()) {
                //there is no more space in the inventory, so we will pop the resource out of the block
                Block.popResourceFromFace(level, blockPos, Direction.UP, outputItem.copy());
                continue;
            }

            ItemStack outputStack = items.get(outputIndex.get());
            if (outputStack.isEmpty()) {
                items.set(outputIndex.get(), outputItem.copy());
            } else {
                outputStack.grow(outputItem.getCount());
            }
        }

        input.shrink(1);
    }

    // gets a itemstack that is the same item with same components as the
    // comparingItem AND if the itemStack has not reached max size
    private static Optional<Integer> getValidItemStack(NonNullList<ItemStack> items, ItemStack comparingItem) {
        Optional<Integer> output = Optional.empty();
        for (int i = 1; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            if (!itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, comparingItem)
                    && itemStack.getCount() < itemStack.getMaxStackSize()) {
                output = Optional.of(i);
                break;
            }
        }
        return output;
    }

    private static Optional<Integer> getEmptyItemStack(NonNullList<ItemStack> items) {
        Optional<Integer> output = Optional.empty();
        for (int i = 1; i < items.size(); i++) {
            ItemStack itemStack = items.get(i);
            if (itemStack.isEmpty()) {
                output = Optional.of(i);
                break;
            }
        }
        return output;
    }

    public static List<ItemStack> getRecycleResult(Level level, ItemStack inputStack) {
        if (level == null || inputStack == null || inputStack.isEmpty()) {
            return List.of();
        }

        // check chache
        if (outputCache.containsKey(inputStack.getItem())) {
            return List.of(outputCache.get(inputStack.getItem()).clone());
        }

        RecipeManager recipeManager = level.getRecipeManager();
        RegistryAccess registryAccess = level.registryAccess();

        List<RecipeHolder<CraftingRecipe>> recipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING);

        for (RecipeHolder<CraftingRecipe> holder : recipes) {
            CraftingRecipe recipe = holder.value();
            ItemStack recipeOutput = recipe.getResultItem(registryAccess);

            // skip if recipe has more than 1 output, because we don't want to recycle 1
            // plank into 1 log for example
            if (recipeOutput.getCount() != 1)
                continue;

            // check if recipe output is our input
            if (!recipeOutput.isEmpty() && recipeOutput.is(inputStack.getItem())) {

                // return the ingredients

                List<ItemStack> returnedItems = new ArrayList<>();
                NonNullList<Ingredient> ingredients = recipe.getIngredients();

                for (Ingredient ingredient : ingredients) {
                    if (ingredient.isEmpty())
                        continue;

                    // Ingredients often use Tags (e.g., "any wood plank").
                    // We can't know which specific wood they used, so we default to the first item
                    // in the tag array.
                    ItemStack[] matchingStacks = ingredient.getItems();
                    if (matchingStacks.length > 0) {
                        ItemStack item = matchingStacks[0].copy();

                        boolean addedItem = false;
                        for (ItemStack returningItem : returnedItems) {
                            if (returningItem.is(item.getItem())) {
                                returningItem.grow(1);
                                addedItem = true;
                                break;
                            }
                        }
                        if (!addedItem)
                            returnedItems.add(item);
                    }
                }

                outputCache.put(inputStack.getItem(), returnedItems.toArray(ItemStack[]::new));
                return returnedItems;
            }
        }

        return List.of(); // Returns empty if no recipe exists
    }
}