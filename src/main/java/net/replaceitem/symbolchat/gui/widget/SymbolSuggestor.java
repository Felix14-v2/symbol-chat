package net.replaceitem.symbolchat.gui.widget;

import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.replaceitem.symbolchat.SymbolChat;
import net.replaceitem.symbolchat.SymbolInsertable;
import net.replaceitem.symbolchat.SymbolStorage;
import net.replaceitem.symbolchat.SymbolSuggestable;
import net.replaceitem.symbolchat.gui.widget.symbolButton.PasteSymbolButtonWidget;
import net.replaceitem.symbolchat.gui.widget.symbolButton.SymbolButtonWidget;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class SymbolSuggestor extends AbstractParentElement implements Drawable, Selectable{
    
    private final Screen screen;
    private final SymbolInsertable insertable;
    private final SymbolSuggestable suggestable;
    
    public static final int HEIGHT = SymbolButtonWidget.SYMBOL_SIZE +2;


    private int x, y;
    private int width;
    private boolean visible;
    private int focusedElement;
    private int elementCount;
    
    private final List<PasteSymbolButtonWidget> symbolButtons = new ArrayList<>();

    public SymbolSuggestor(Screen screen, SymbolInsertable insertable, SymbolSuggestable suggestable) {
        this.screen = screen;
        this.insertable = insertable;
        this.suggestable = suggestable;
        this.elementCount = 0;
        this.focusedElement = -1;
        visible = false;
    }

    public void refresh() {
        Vector2i cursorPosition = this.suggestable.getCursorPosition();
        this.x = cursorPosition.x;
        this.y = cursorPosition.y - HEIGHT - 3;
        
        String search = this.suggestable.getSuggestionTerm();

        
        int fittingSymbols = Math.floorDiv(this.screen.width, SymbolButtonWidget.SYMBOL_SIZE + 1);
        int shownSymbols = Math.min(fittingSymbols, SymbolChat.config.getMaxSymbolSuggestions());
        
        symbolButtons.clear();
        List<String> symbols = SymbolStorage.performSearch(search).limit(shownSymbols).toList();
        
        elementCount = symbols.size();

        this.width = 1 + (SymbolButtonWidget.SYMBOL_SIZE + 1) * elementCount;
        this.x = Math.max(Math.min(this.x, this.screen.width - this.width), 0);
        
        visible = !symbols.isEmpty();
        
        for (int i = 0; i < elementCount; i++) {
            symbolButtons.add(new PasteSymbolButtonWidget(this.x+1+i*(SymbolButtonWidget.SYMBOL_SIZE+1), this.y+1, insertable, symbols.get(i)));
        }
        
        setFocusedElement(-1);
    }
    
    private void setFocusedElement(int focused) {
        if(this.getFocused() != null) ((PasteSymbolButtonWidget) this.getFocused()).makeFocused(false);
        this.focusedElement = MathHelper.clamp(focused, 0, elementCount-1);
        PasteSymbolButtonWidget focus = focusedElement < 0 || focused < 0 ? null : this.symbolButtons.get(focusedElement);
        if(focus != null) focus.makeFocused(true);
        this.setFocused(focus);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(!visible) return;
        fill(matrices, this.x, this.y, this.x+width, this.y+HEIGHT, SymbolChat.config.getHudColor());
        for (Drawable drawable : symbolButtons) {
            drawable.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_TAB) {
            PasteSymbolButtonWidget focused;
            if(this.getFocused() instanceof PasteSymbolButtonWidget pasteSymbolButtonWidget) {
                focused = pasteSymbolButtonWidget;
            } else if(!this.symbolButtons.isEmpty()) {
                focused = this.symbolButtons.get(0);
            } else return false;
            focused.onClick(0,0);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_UP && this.getFocused() == null && !this.symbolButtons.isEmpty()) {
            this.setFocusedElement(0);
            return true;
        }
        
        if(keyCode == GLFW.GLFW_KEY_RIGHT && this.getFocused() != null) {
            this.setFocusedElement(this.focusedElement+1);
            return true;
        }
        if(keyCode == GLFW.GLFW_KEY_LEFT && this.getFocused() != null) {
            if(this.focusedElement != 0) this.setFocusedElement(this.focusedElement-1);
            return true;
        }
        if((keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_ESCAPE) && this.getFocused() != null) {
            this.setFocusedElement(-1);
            return true;
        }
        
        if(keyCode == GLFW.GLFW_KEY_ENTER && this.getFocused() != null) {
            this.symbolButtons.get(focusedElement).onClick(0,0);
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        
    }

    @Override
    public List<? extends Element> children() {
        return symbolButtons;
    }
}
