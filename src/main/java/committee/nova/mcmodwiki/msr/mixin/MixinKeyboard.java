package committee.nova.mcmodwiki.msr.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static committee.nova.mcmodwiki.msr.client.MSRClient.keyBind;
import static committee.nova.mcmodwiki.msr.utils.Utilities.openScreen;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard {
    @Inject(method = "onKey", at = @At("HEAD"))
    public void keyPressed(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        final var pressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), keyBind.getDefaultKey().getCode());
        if (!pressed) return;
        openScreen();
    }
}
