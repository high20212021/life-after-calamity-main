package moe.yukari.lifeaftercalamity.tools;

import moe.yukari.lifeaftercalamity.LifeAfterCalamity;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ChunkEaterItemMaterial implements ToolMaterial {
    @Override
    public int getDurability() {
        return 2147483647;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 32767.0F;
    }

    @Override
    public float getAttackDamage() {
        return -1;
    }

    @Override
    public int getMiningLevel() {
        return 5;
    }

    @Override
    public int getEnchantability() {
        return 2147483647;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(LifeAfterCalamity.ANCIENT_INGOT);
    }
    
}
