package net.replaceitem.symbolchat.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Narratable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.replaceitem.symbolchat.SymbolChat;

import java.util.ArrayList;
import java.util.List;

public class DropDownWidget<T> extends ClickableWidget implements Drawable, Element, Narratable {

    public final List<DropDownElementWidget<T>> elements;
    public int selected;
    public boolean expanded;

    public DropDownWidget(int x, int y, int width, int height, List<T> elementList, int defaultSelection) {
        super(x, y, width, height, Text.empty());
        this.elements = new ArrayList<>();
        for(int i = 0; i < elementList.size(); i++) {
            int dy = this.getY() + this.height + 1 + i*(this.height+1);
            this.elements.add(new DropDownElementWidget<>(this.getX() + 1, dy, this.width - 2, this.height, elementList.get(i), i, this));
        }
        this.expanded = false;
        this.selected = defaultSelection;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.visible) {
            RenderSystem.disableDepthTest();
            fill(matrices, this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, SymbolChat.config.getButtonColor());
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            int z = this.isHovered() ? 16777215 : 10526880;
            drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, z | MathHelper.ceil(this.alpha * 255.0F) << 24);

            if(this.expanded) {
                fill(matrices, this.getX(), this.getY()+this.height, this.getX() + this.width, this.getY() + this.height + 1 + this.elements.size()*(this.height+1), SymbolChat.config.getHudColor());
                for(DropDownElementWidget<?> element : elements) {
                    element.render(matrices,mouseX,mouseY,delta);
                }
            }
        }
    }

    @Override
    public Text getMessage() {
        return this.elements.get(selected).getMessage();
    }
    
    public Text getTextForElement(T element) {
        return Text.literal(element.toString());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.expanded = !this.expanded;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.expanded) {
            for(DropDownElementWidget<?> element : elements) {
                if(element.mouseClicked(mouseX,mouseY,button)) return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void changeSelected(int index) {
        this.selected = index;
        this.expanded = false;
        this.onSelection(index, elements.get(index).getElement());
    }

    public void onSelection(int index, T element) {}

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
        builder.put(NarrationPart.HINT, "Dropdown: " + getSelection().toString());
    }
    public T getSelection() {
        return this.elements.get(selected).getElement();
    }
}
