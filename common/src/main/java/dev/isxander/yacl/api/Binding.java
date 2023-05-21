package dev.isxander.yacl.api;

import dev.isxander.yacl.impl.GenericBindingImpl;
import org.apache.commons.lang3.Validate;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Controls modifying the bound option.
 * Provides the default value, a setter and a getter.
 */
public interface Binding<T> {
    void setValue(T value);

    T getValue();

    T defaultValue();

    /**
     * Creates a generic binding.
     *
     * @param def default value of the option, used to reset
     * @param getter should return the current value of the option
     * @param setter should set the option to the supplied value
     */
    static <T> Binding<T> generic(T def, Supplier<T> getter, Consumer<T> setter) {
        Validate.notNull(def, "`def` must not be null");
        Validate.notNull(getter, "`getter` must not be null");
        Validate.notNull(setter, "`setter` must not be null");

        return new GenericBindingImpl<>(def, getter, setter);
    }

    /**
     * Creates an immutable binding that has no default and cannot be modified.
     *
     * @param value the value for the binding
     */
    static <T> Binding<T> immutable(T value) {
        Validate.notNull(value, "`value` must not be null");

        return new GenericBindingImpl<>(
                value,
                () -> value,
                changed -> {}
        );
    }
}
