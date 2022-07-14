package committee.nova.mcmodwiki;

import com.mojang.blaze3d.platform.InputUtil;
import committee.nova.mcmodwiki.core.CoreService;
import committee.nova.mcmodwiki.mixin.HandledScreenAccessor;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBind;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSRClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("MSR");
    public static KeyBind keyBind;

    @Override
    public void onInitializeClient(ModContainer mod) {
        keyBind = KeyBindingHelper.registerKeyBinding(new KeyBind(
                "key.gui.search",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.msr"
        ));
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
    }
}
