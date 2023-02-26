package com.github.klee319.customitem.customitem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CustomTagRecipeChoice extends RecipeChoice.MaterialChoice {
    private final ItemStack itemStack;

    public CustomTagRecipeChoice(ItemStack itemStack) {
        super(itemStack.getType());
        this.itemStack = itemStack;

    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public @NotNull CustomTagRecipeChoice clone() {
        return (CustomTagRecipeChoice) super.clone();
    }

    @Override
    public boolean test(@NotNull ItemStack itemStack) {
        // ここで与えられたアイテムをチェックする。OKならtrueを返す
        return (Objects.equals(CustomTag.getCustomTag(itemStack,"CustomTagParticle"), "Onion")) && (itemStack.getType() == Material.GOLD_INGOT);
    }
}
