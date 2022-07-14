package committee.nova.mcmodwiki;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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
}
