package net.replaceitem.symbolchat.gui.container;

import net.minecraft.client.gui.widget.LayoutWidget;

public abstract class ScrollableLayoutContainer<T extends LayoutWidget> extends SmoothScrollableContainerWidget {

    protected T layout;

    public ScrollableLayoutContainer(int x, int y, int width, int height, T layout) {
        super(x, y, width, height);
        this.layout = layout;
    }

    public void refreshPositions() {
       this.layout.refreshPositions();
        this.updateChildPos();
    }

    public void updateChildPos() {
        int childX = this.getX();
        int childY = this.getY() - (int) getScrollY();
        if(layout.getX() != childX) layout.setX(childX);
        if(layout.getY() != childY) layout.setY(childY);
    }

    @Override
    protected int getContentsHeightWithPadding() {
        return this.layout.getHeight();
    }

    @Override
    public void setScrollY(double scrollY) {
        super.setScrollY(scrollY);
        this.updateChildPos();
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.refreshPositions();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.refreshPositions();
    }
}
