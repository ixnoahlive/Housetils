package live.ixnoah.housetils.mixin;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.impl.command.client.ClientCommandInternals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.util.StringHelper;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    public static boolean hasShiftDown() {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344);
    }

    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "handleTextClick", at = @At("HEAD"))
    @SuppressWarnings("UnstableApiUsage")
    public void housetils_handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null) {
            ClickEvent clickEvent = style.getClickEvent();

            if (!hasShiftDown() && clickEvent != null && Objects.equals(String.valueOf(clickEvent.getAction()), "run_command_client")) {
                String string = StringHelper.stripInvalidChars(clickEvent.getValue());
                if (string.startsWith("/")) {
                    if (!ClientCommandInternals.executeCommand(string.substring(1))) {
                        LOGGER.error("Not allowed to run command with signed argument from click event: '{}'", string);
                    }
                } else {
                    LOGGER.error("Failed to run command without '/' prefix from click event: '{}'", string);
                }
            }
        }
    }
}
