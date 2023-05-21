package dev.isxander.yacl.test;

import dev.isxander.yacl.api.*;
import dev.isxander.yacl.gui.RequireRestartScreen;
import dev.isxander.yacl.gui.controllers.*;
import dev.isxander.yacl.gui.controllers.cycling.EnumController;
import dev.isxander.yacl.gui.controllers.slider.DoubleSliderController;
import dev.isxander.yacl.gui.controllers.slider.FloatSliderController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl.gui.controllers.slider.LongSliderController;
import dev.isxander.yacl.gui.controllers.string.StringController;
import dev.isxander.yacl.gui.controllers.string.number.DoubleFieldController;
import dev.isxander.yacl.gui.controllers.string.number.FloatFieldController;
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController;
import dev.isxander.yacl.gui.controllers.string.number.LongFieldController;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;

import java.awt.Color;
import java.util.List;

public class GuiTest {
    public static Screen getModConfigScreenFactory(Screen parent) {
        return YetAnotherConfigLib.create(ConfigTest.GSON, (defaults, config, builder) -> builder
                        .title(new TextComponent("Test Suites"))
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Suites"))
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Full Test Suite"))
                                        .controller(ActionController::new)
                                        .action((screen, opt) -> Minecraft.getInstance().setScreen(getFullTestSuite(screen)))
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("Wiki"))
                                        .option(ButtonOption.createBuilder()
                                                .name(new TextComponent("Get Started"))
                                                .controller(ActionController::new)
                                                .action((screen, opt) -> Minecraft.getInstance().setScreen(getWikiGetStarted(screen)))
                                                .build())
                                        .build())
                                .build())
                )
                .generateScreen(parent);
    }

    private static Screen getFullTestSuite(Screen parent) {
        return YetAnotherConfigLib.create(ConfigTest.GSON, (defaults, config, builder) -> builder
                        .title(new TextComponent("Test GUI"))
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Control Examples"))
                                .tooltip(new TextComponent("Example Category Description"))
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("Boolean Controllers"))
                                        .tooltip(new TextComponent("Test!"))
                                        .option(Option.createBuilder(boolean.class)
                                                .name(new TextComponent("Boolean Toggle"))
                                                .tooltip(value -> new TextComponent("A simple toggle button that contains the value '" + value + "'"))
                                                .binding(
                                                        defaults.booleanToggle,
                                                        () -> config.booleanToggle,
                                                        (value) -> config.booleanToggle = value
                                                )
                                                .controller(BooleanController::new)
                                                .flag(OptionFlag.GAME_RESTART)
                                                .build())
                                        .option(Option.createBuilder(boolean.class)
                                                .name(new TextComponent("Custom Boolean Toggle"))
                                                .tooltip(new TextComponent("You can customize these controllers like this!"))
                                                .tooltip(TextComponent.EMPTY)
                                                .tooltip(opt -> TextComponent.EMPTY)
                                                .binding(
                                                        defaults.customBooleanToggle,
                                                        () -> config.customBooleanToggle,
                                                        (value) -> config.customBooleanToggle = value
                                                )
                                                .controller(opt -> new BooleanController(opt, state -> state ? new TextComponent("Amazing") : new TextComponent("Not Amazing"), true))
                                                .build())
                                        .option(Option.createBuilder(boolean.class)
                                                .name(new TextComponent("Tick Box"))
                                                .tooltip(new TextComponent("There are even alternate methods of displaying the same data type!"))
                                                .binding(
                                                        defaults.tickbox,
                                                        () -> config.tickbox,
                                                        (value) -> config.tickbox = value
                                                )
                                                .controller(TickBoxController::new)
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("Slider Controllers"))
                                        .option(Option.createBuilder(int.class)
                                                .name(new TextComponent("Int Slider"))
                                                .instant(true)
                                                .binding(
                                                        defaults.intSlider,
                                                        () -> config.intSlider,
                                                        value -> config.intSlider = value

                                                )
                                                .controller(opt -> new IntegerSliderController(opt, 0, 3, 1))
                                                .build())
                                        .option(Option.createBuilder(double.class)
                                                .name(new TextComponent("Double Slider"))
                                                .binding(
                                                        defaults.doubleSlider,
                                                        () -> config.doubleSlider,
                                                        (value) -> config.doubleSlider = value
                                                )
                                                .controller(opt -> new DoubleSliderController(opt, 0, 3, 0.05))
                                                .build())
                                        .option(Option.createBuilder(float.class)
                                                .name(new TextComponent("Float Slider"))
                                                .binding(
                                                        defaults.floatSlider,
                                                        () -> config.floatSlider,
                                                        (value) -> config.floatSlider = value
                                                )
                                                .controller(opt -> new FloatSliderController(opt, 0, 3, 0.1f))
                                                .build())
                                        .option(Option.createBuilder(long.class)
                                                .name(new TextComponent("Long Slider"))
                                                .binding(
                                                        defaults.longSlider,
                                                        () -> config.longSlider,
                                                        (value) -> config.longSlider = value
                                                )
                                                .controller(opt -> new LongSliderController(opt, 0, 1_000_000, 100))
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("Input Field Controllers"))
                                        .option(Option.createBuilder(String.class)
                                                .name(new TextComponent("Component Option"))
                                                .binding(
                                                        defaults.textField,
                                                        () -> config.textField,
                                                        value -> config.textField = value
                                                )
                                                .controller(StringController::new)
                                                .build())
                                        .option(Option.createBuilder(Color.class)
                                                .name(new TextComponent("Color Option"))
                                                .binding(
                                                        defaults.colorOption,
                                                        () -> config.colorOption,
                                                        value -> config.colorOption = value
                                                )
                                                .controller(ColorController::new)
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("Number Fields"))
                                        .option(Option.createBuilder(double.class)
                                                .name(new TextComponent("Double Field"))
                                                .binding(
                                                        defaults.doubleField,
                                                        () -> config.doubleField,
                                                        value -> config.doubleField = value
                                                )
                                                .controller(DoubleFieldController::new)
                                                .build())
                                        .option(Option.createBuilder(float.class)
                                                .name(new TextComponent("Float Field"))
                                                .binding(
                                                        defaults.floatField,
                                                        () -> config.floatField,
                                                        value -> config.floatField = value
                                                )
                                                .controller(FloatFieldController::new)
                                                .build())
                                        .option(Option.createBuilder(int.class)
                                                .name(new TextComponent("Integer Field"))
                                                .binding(
                                                        defaults.intField,
                                                        () -> config.intField,
                                                        value -> config.intField = value
                                                )
                                                .controller(IntegerFieldController::new)
                                                .build())
                                        .option(Option.createBuilder(long.class)
                                                .name(new TextComponent("Long Field"))
                                                .binding(
                                                        defaults.longField,
                                                        () -> config.longField,
                                                        value -> config.longField = value
                                                )
                                                .controller(LongFieldController::new)
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("Enum Controllers"))
                                        .option(Option.createBuilder(ConfigTest.Alphabet.class)
                                                .name(new TextComponent("Enum Cycler"))
                                                .binding(
                                                        defaults.enumOption,
                                                        () -> config.enumOption,
                                                        (value) -> config.enumOption = value
                                                )
                                                .controller(EnumController::new)
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("Options that aren't really options"))
                                        .option(ButtonOption.createBuilder()
                                                .name(new TextComponent("Button \"Option\""))
                                                .action((screen, opt) -> SystemToast.add(Minecraft.getInstance().getToasts(), SystemToast.SystemToastIds.TUTORIAL_HINT, new TextComponent("Button Pressed"), new TextComponent("Button option was invoked!")))
                                                .controller(ActionController::new)
                                                .build())
                                        .option(LabelOption.create(
                                                TextComponent.EMPTY.plainCopy()
                                                        .append(new TextComponent("a").withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("a")))))
                                                        .append(new TextComponent("b").withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("b")))))
                                                        .append(new TextComponent("c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c c").withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("c")))))
                                                        .append(new TextComponent("e").withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("e")))))
                                                        .withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://isxander.dev"))))
                                        )
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("List Test"))
                                .group(ListOption.createBuilder(String.class)
                                        .name(new TextComponent("String List"))
                                        .binding(
                                                defaults.stringList,
                                                () -> config.stringList,
                                                val -> config.stringList = val
                                        )
                                        .controller(StringController::new)
                                        .initial("")
                                        .build())
                                .group(ListOption.createBuilder(Integer.class)
                                        .name(new TextComponent("Slider List"))
                                        .binding(
                                                defaults.intList,
                                                () -> config.intList,
                                                val -> config.intList = val
                                        )
                                        .controller(opt -> new IntegerSliderController(opt, 0, 10, 1))
                                        .initial(0)
                                        .available(false)
                                        .build())
                                .group(ListOption.createBuilder(Component.class)
                                        .name(new TextComponent("Useless Label List"))
                                        .binding(Binding.immutable(List.of(new TextComponent("It's quite impressive that literally every single controller works, without problem."))))
                                        .controller(LabelController::new)
                                        .initial(new TextComponent("Initial label"))
                                        .build())
                                .build())
                        .category(PlaceholderCategory.createBuilder()
                                .name(new TextComponent("Placeholder Category"))
                                .screen((client, yaclScreen) -> new RequireRestartScreen(yaclScreen))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Group Test"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(new TextComponent("Root Test"))
                                        .binding(
                                                defaults.groupTestRoot,
                                                () -> config.groupTestRoot,
                                                value -> config.groupTestRoot = value
                                        )
                                        .controller(TickBoxController::new)
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(new TextComponent("First Group"))
                                        .option(Option.createBuilder(boolean.class)
                                                .name(new TextComponent("First Group Test 1"))
                                                .binding(
                                                        defaults.groupTestFirstGroup,
                                                        () -> config.groupTestFirstGroup,
                                                        value -> config.groupTestFirstGroup = value
                                                )
                                                .controller(TickBoxController::new)
                                                .build())
                                        .option(Option.createBuilder(boolean.class)
                                                .name(new TextComponent("First Group Test 2"))
                                                .binding(
                                                        defaults.groupTestFirstGroup2,
                                                        () -> config.groupTestFirstGroup2,
                                                        value -> config.groupTestFirstGroup2 = value
                                                )
                                                .controller(TickBoxController::new)
                                                .build())
                                        .build())
                                .group(OptionGroup.createBuilder()
                                        .name(TextComponent.EMPTY)
                                        .option(Option.createBuilder(boolean.class)
                                                .name(new TextComponent("Second Group Test"))
                                                .binding(
                                                        defaults.groupTestSecondGroup,
                                                        () -> config.groupTestSecondGroup,
                                                        value -> config.groupTestSecondGroup = value
                                                )
                                                .controller(TickBoxController::new)
                                                .build())
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Scroll Test"))
                                .option(Option.createBuilder(int.class)
                                        .name(new TextComponent("Int Slider that is cut off because the slider"))
                                        .binding(
                                                defaults.scrollingSlider,
                                                () -> config.scrollingSlider,
                                                (value) -> config.scrollingSlider = value
                                        )
                                        .controller(opt -> new IntegerSliderController(opt, 0, 10, 1))
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(new TextComponent("Option"))
                                        .action((screen, opt) -> {
                                        })
                                        .controller(ActionController::new)
                                        .build())
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .category(ConfigCategory.createBuilder()
                                .name(new TextComponent("Category Test"))
                                .build())
                        .save(() -> {
                            Minecraft.getInstance().options.save();
                            ConfigTest.GSON.save();
                        })
                )
                .generateScreen(parent);
    }

    private static boolean myBooleanOption = true;

    private static Screen getWikiGetStarted(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(new TextComponent("Used for narration. Could be used to render a title in the future."))
                .category(ConfigCategory.createBuilder()
                        .name(new TextComponent("Name of the category"))
                        .tooltip(new TextComponent("This Component will appear as a tooltip when you hover or focus the button with Tab. There is no need to add \n to wrap as YACL will do it for you."))
                        .group(OptionGroup.createBuilder()
                                .name(new TextComponent("Name of the group"))
                                .tooltip(new TextComponent("This Component will appear when you hover over the name or focus on the collapse button with Tab."))
                                .option(Option.createBuilder(boolean.class)
                                        .name(new TextComponent("Boolean Option"))
                                        .tooltip(new TextComponent("This Component will appear as a tooltip when you hover over the option."))
                                        .binding(true, () -> myBooleanOption, newVal -> myBooleanOption = newVal)
                                        .controller(TickBoxController::new)
                                        .build())
                                .build())
                        .build())
                .build()
                .generateScreen(parent);
    }
}
