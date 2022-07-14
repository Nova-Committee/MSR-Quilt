package committee.nova.mcmodwiki.utils;

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
