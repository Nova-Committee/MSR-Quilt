package committee.nova.mcmodwiki.msr.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class MSRClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("MSR");
    public static KeyBinding keyBind;

    @Override
    public void onInitializeClient() {
        keyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.gui.search",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.msr"
        ));
    }
}
