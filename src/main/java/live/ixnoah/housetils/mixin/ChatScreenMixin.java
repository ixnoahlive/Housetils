package live.ixnoah.housetils.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatScreen.class)
public interface ChatScreenMixin {
    @Accessor
    TextFieldWidget getChatField();

    @Accessor("chatField")
    void setChatField(TextFieldWidget chatField);
}