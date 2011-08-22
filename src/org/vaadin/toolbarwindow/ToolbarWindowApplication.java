package org.vaadin.toolbarwindow;

import org.vaadin.jouni.animator.AnimatorProxy;
import org.vaadin.jouni.animator.AnimatorProxy.AnimationEvent;
import org.vaadin.jouni.animator.client.ui.VAnimatorProxy.AnimType;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class ToolbarWindowApplication extends Application {

    private float lastWidth;
    private float lastHeight;
    private int lastX;
    private int lastY;

    private AnimatorProxy ap = new AnimatorProxy();

    @Override
    public void init() {
        final Window mainWindow = new Window("ToolbarWindow Demo");
        setMainWindow(mainWindow);
        mainWindow.getContent().setSizeFull();
        mainWindow.addComponent(ap);

        final Button reopen = new Button("Re-open window");

        final ToolbarWindow w = new ToolbarWindow("Window w/ Tools");
        w.center();
        w.setWidth("400px");
        w.setHeight("300px");

        w.setContent(buildContent());

        HorizontalLayout tools = new HorizontalLayout();
        tools.addStyleName(Reindeer.LAYOUT_BLACK);
        tools.setSpacing(true);

        Button b = new Button("+ New", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                mainWindow.addWindow(new Window("Just a normal window"));
            }
        });
        b.setDescription("Open a new window");
        b.addStyleName(Reindeer.BUTTON_SMALL);
        tools.addComponent(b);

        ap.addListener(new AnimatorProxy.AnimationListener() {
            public void onAnimation(AnimationEvent event) {
                if (event.getAnimation().getTarget() == w
                        && event.getAnimation().getType() == AnimType.FADE_OUT) {
                    w.setVisible(false);
                    reopen.setVisible(true);
                }
            }
        });

        b = new Button(null, new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                lastWidth = w.getWidth();
                lastHeight = w.getHeight();
                lastX = w.getPositionX();
                lastY = w.getPositionY();
                ap.animate(w, AnimType.SIZE).setData("x=-200,y=+200")
                        .setDuration(150);
                ap.animate(w, AnimType.FADE_OUT).setDuration(150);
            }
        });
        b.setDescription("Minimize");
        b.setIcon(new ClassResource("minimize.png", this));
        b.addStyleName(Reindeer.BUTTON_LINK);
        tools.addComponent(b);

        b = new Button(null, new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                if (w.getWidth() != 100) {
                    lastWidth = w.getWidth();
                    lastHeight = w.getHeight();
                    lastX = w.getPositionX();
                    lastY = w.getPositionY();
                    w.setSizeFull();
                    w.center();
                } else {
                    w.setWidth(lastWidth, Window.UNITS_PIXELS);
                    w.setHeight(lastHeight, Window.UNITS_PIXELS);
                    w.setPositionX(lastX);
                    w.setPositionY(lastY);
                }
                event.getButton().focus();
            }
        });
        b.setDescription("Maximize");
        b.setIcon(new ClassResource("maximize.png", this));
        b.addStyleName(Reindeer.BUTTON_LINK);
        tools.addComponent(b);

        w.setToolbar(tools);

        getMainWindow().addWindow(w);

        mainWindow.addComponent(reopen);
        ((VerticalLayout) mainWindow.getContent()).setComponentAlignment(
                reopen, Alignment.BOTTOM_LEFT);
        reopen.setVisible(false);

        reopen.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                w.setVisible(true);
                w.setWidth(lastWidth, Window.UNITS_PIXELS);
                w.setHeight(lastHeight, Window.UNITS_PIXELS);
                w.setPositionX(lastX);
                w.setPositionY(lastY);
                reopen.setVisible(false);
            }
        });
    }

    private ComponentContainer buildContent() {
        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName(Reindeer.TABSHEET_SMALL);

        CssLayout wrap = new CssLayout();
        wrap.setMargin(true);
        wrap.setWidth("100%");
        wrap.addComponent(new Label(
                "<p><strong>The size of the window should be specified, otherwise it will overflow the content when the user resizes the window smaller for the first time.</strong></p><p>After the first time, everything should work normally.</p><p>Morbi euismod magna ac lorem rutrum elementum. Donec viverra auctor lobortis. Pellentesque eu est a nulla placerat dignissim. Morbi a enim in magna semper bibendum. Etiam scelerisque, nunc ac egestas consequat, odio nibh euismod nulla, eget auctor orci nibh vel nisi. Aliquam erat volutpat. Mauris vel neque sit amet nunc gravida congue sed sit amet purus. Quisque lacus quam, egestas ac tincidunt a, lacinia vel velit. Aenean facilisis nulla vitae urna.</p>",
                Label.CONTENT_XHTML));
        tabs.addTab(wrap, "Intro", null);

        Table t = new Table();
        t.addStyleName(Reindeer.TABLE_BORDERLESS);
        t.setSizeFull();
        t.addContainerProperty("Foo", String.class, null);
        t.addContainerProperty("Bar", String.class, null);
        for (int i = 1; i <= 1000; i++) {
            Item item = t.addItem(i);
            item.getItemProperty("Foo").setValue("Foo value " + i);
            item.getItemProperty("Bar").setValue("Bar value " + i);
        }
        tabs.addTab(t, "Dummy Table", null);

        return tabs;
    }
}
