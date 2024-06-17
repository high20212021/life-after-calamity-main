package moe.yukari.lifeaftercalamity.tools;

import moe.yukari.lifeaftercalamity.LifeAfterCalamity;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class CalamityItemMaterial implements ToolMaterial{
    @Override
    public int getDurability() {
        return 71;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 1.0F;
    }

    @Override
    public float getAttackDamage() {
        return -1;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 1;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(LifeAfterCalamity.CALAMITY_CORE);
    }
}
