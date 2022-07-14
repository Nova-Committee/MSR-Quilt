package committee.nova.mcmodwiki.msr.utils;

import committee.nova.mcmodwiki.msr.client.MSRClient;
import committee.nova.mcmodwiki.msr.core.CoreService;
import committee.nova.mcmodwiki.msr.mixin.HandledScreenAccessor;
import mezz.jei.common.Internal;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Utilities {
    @Nullable
    public static String getDefaultCreatorModId(@Nonnull ItemStack itemStack) {
        final var item = itemStack.getItem();
        final var translationKey = item.getTranslationKey();
        final var modId = translationKey == null ? null : getNamespace(translationKey);
        if (!("minecraft".equals(modId))) return modId;
        if (item instanceof EnchantedBookItem) {
            final var enchantmentsNbt = EnchantedBookItem.getEnchantmentNbt(itemStack);
            if (enchantmentsNbt.size() >= 1) {
                final var nbt = enchantmentsNbt.getCompound(0);
                final var eid = Identifier.tryParse(nbt.getString("id"));
                if (eid != null && Registry.ENCHANTMENT.containsId(eid)) return eid.getNamespace();
            }
        }
        if (item instanceof PotionItem || item instanceof TippedArrowItem) {
            final var potionType = PotionUtil.getPotion(itemStack);
            final var pid = Registry.POTION.getId(potionType);
            if (pid != Registry.POTION.getDefaultId()) return pid.getNamespace();
        }
        if (item instanceof SpawnEggItem)
            return getNamespace(((SpawnEggItem) item).getEntityType(null).getTranslationKey());
        return modId;
    }

    public static void openScreen() {
        final var screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof HandledScreen gui) {
            final var slot = ((HandledScreenAccessor) gui).getFocusedSlot();
            if (slot != null) {
                final var itemStack = slot.getStack();
                if (!itemStack.isEmpty()) CoreService.tryOpen(itemStack);
            }
        }
        if (!FabricLoader.getInstance().isModLoaded("jei")) return;
        final var jei = Internal.getRuntime();
        if (jei.isEmpty()) {
            MSRClient.LOGGER.warn("Unable to get JEI item >> JEI Runtime Loss");
            return;
        }
        try {
            final var ingredientType = Internal.getRegisteredIngredients().getIngredientType(ItemStack.class);
            CoreService.tryOpen(jei.get().getIngredientListOverlay().getIngredientUnderMouse(ingredientType));
        } catch (Throwable ex) {
            MSRClient.LOGGER.warn("Unable to get JEI item >> Exception Caught", ex);
        }
    }

    public static String getNamespace(String key) {
        final var s1 = StringUtils.substringAfter(key, ".");
        return StringUtils.substringBefore(s1, ".");
    }

    public static String getId(String key) {
        final var s1 = StringUtils.substringAfter(key, ".");
        return StringUtils.substringAfter(s1, ".");
    }

    public static String getIdentifier(String key) {
        return getNamespace(key) + ":" + getId(key);
    }

}
