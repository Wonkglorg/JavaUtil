package com.wonkglorg.util.reflection;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public enum Modifier {

    PUBLIC("public", java.lang.reflect.Modifier.PUBLIC),
    PROTECTED("protected", java.lang.reflect.Modifier.PROTECTED),
    PRIVATE("private", java.lang.reflect.Modifier.PRIVATE),
    ABSTRACT("abstract", java.lang.reflect.Modifier.ABSTRACT),
    STATIC("static", java.lang.reflect.Modifier.STATIC),
    FINAL("final", java.lang.reflect.Modifier.FINAL),
    TRANSIENT("transient", java.lang.reflect.Modifier.TRANSIENT),
    VOLATILE("volatile", java.lang.reflect.Modifier.VOLATILE),
    SYNCHRONIZED("synchronized", java.lang.reflect.Modifier.SYNCHRONIZED),
    NATIVE("native", java.lang.reflect.Modifier.NATIVE),
    STRICT("strictfp", java.lang.reflect.Modifier.STRICT),
    INTERFACE("interface", java.lang.reflect.Modifier.INTERFACE),
    ;
    private final String name;
    private final int modifierValue;

    Modifier(String name, int modifier) {
        this.name = name;
        this.modifierValue = modifier;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns a list of modifiers that are present in the given modifier value
     *
     * @param mod The modifier value to check
     * @return A list of modifiers that are present in the given modifier value
     */
    public static List<Modifier> getModifiers(int mod) {
        List<Modifier> modifiers = new ArrayList<>();
        for (Modifier modifier : Modifier.values()) {
            if ((mod & modifier.modifierValue) != 0) {
                modifiers.add(modifier);
            }
        }
        return modifiers;
    }

    public String getName() {
        return name;
    }

    public int getModifierValue() {
        return modifierValue;
    }


}
