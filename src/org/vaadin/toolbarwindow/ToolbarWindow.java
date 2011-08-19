package org.vaadin.toolbarwindow;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.vaadin.cssinject.CSSInject;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class ToolbarWindow extends Window {

    private Component toolbar;
    private CssLayout wrapper = new CssLayout();
    private Panel scroll = new Panel();
    private CSSInject css = new CSSInject();

    private static String cssText;

    public ToolbarWindow() {
        super();

        try {
            cssText = readFileAsString("toolbarwindow.css");
        } catch (IOException e1) {
            System.err
                    .println("ToolbarWindow failed to load additional CSS from the classpath (toolbarwindow.css)");
        }

        // Force size updates to be immediate so that contained layouts can
        // adjust after initial render
        setImmediate(true);

        wrapper.setSizeUndefined();
        scroll.setSizeUndefined();

        addStyleName("toolbarwindow");
        wrapper.addStyleName("toolbarwindow-wrap");
        scroll.addStyleName("toolbarwindow-scroll");
        scroll.addStyleName(Reindeer.PANEL_LIGHT);

        ComponentContainer oldContent = getContent();

        super.setContent(wrapper);
        wrapper.addComponent(css);
        wrapper.addComponent(scroll);

        setContent(oldContent);

        String styles = getAdditionalCss();
        if (styles != null) {
            css.setValue(styles);
        }

    }

    public ToolbarWindow(String caption) {
        this();
        setCaption(caption);
    }

    protected String getAdditionalCss() {
        return cssText;
    }

    private static String readFileAsString(String filePath)
            throws java.io.IOException {
        byte[] buffer = new byte[20480];
        BufferedInputStream f = new BufferedInputStream(
                ToolbarWindow.class.getResourceAsStream(filePath));
        int read = f.read(buffer);
        byte[] next = new byte[read];
        for (int i = 0; i < read; i++) {
            next[i] = buffer[i];
        }
        return new String(next);
    }

    @Override
    public void setContent(ComponentContainer content) {
        if (scroll != null) {
            scroll.setContent(content);
        }
    }

    @Override
    public void addComponent(Component c) {
        scroll.addComponent(c);
    }

    @Override
    public void removeComponent(Component c) {
        scroll.removeComponent(c);
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        return scroll.getComponentIterator();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        scroll.replaceComponent(oldComponent, newComponent);
    }

    @Override
    public void removeAllComponents() {
        scroll.removeAllComponents();
    }

    @Override
    public void setWidth(float width, int unit) {
        super.setWidth(width, unit);

        if (wrapper == null) {
            return;
        }

        if (width > 0) {
            wrapper.setWidth("100%");
            scroll.setWidth("100%");
        } else {
            wrapper.setWidth("-1px");
            scroll.setWidth("-1px");
        }
    }

    @Override
    public void setHeight(float height, int unit) {
        super.setHeight(height, unit);

        if (wrapper == null) {
            return;
        }

        if (height > 0) {
            wrapper.setHeight("100%");
            scroll.setHeight("100%");
        } else {
            wrapper.setHeight("-1px");
            scroll.setHeight("-1px");
        }
    }

    public void setToolbar(Component toolbar) {
        if (this.toolbar != null) {
            wrapper.removeComponent(this.toolbar);
        }
        this.toolbar = toolbar;
        this.toolbar.addStyleName("toolbarwindow-toolbar");
        wrapper.addComponent(toolbar, 1);
    }

    public Component getToolbar() {
        return toolbar;
    }

}
