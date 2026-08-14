package saurerstoff.quality.enchantment;

public class EnchantmentState {
    public static final ThreadLocal<Integer> CURRENT_QUALITY = ThreadLocal.withInitial(() -> 0);
}
