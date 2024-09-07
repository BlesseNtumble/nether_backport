package com.unseen.nb.common.items.netherite;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.unseen.nb.Main;
import com.unseen.nb.handler.IHasModel;
import com.unseen.nb.init.ModItems;
import com.unseen.nb.util.ModReference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class ArmorBase extends ItemArmor implements IHasModel {
    private String texture;

    private final AttributeModifier knockbackResistance;
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("a3578781-e4a8-4d70-9d32-cd952aeae1df"),
            UUID.fromString("e2d1f056-f539-48c7-b353-30d7a367ebd0"), UUID.fromString("db13047a-bb47-4621-a025-65ed22ce461a"),
            UUID.fromString("abb5df20-361d-420a-8ec7-4bdba33378eb")};

    private String armorBonusDesc = "";
    public ArmorBase(String name, ArmorMaterial materialIn, int renderIdx, EntityEquipmentSlot slotIn, String textureName) {
        super(materialIn, renderIdx, slotIn);
        setCreativeTab(CreativeTabs.COMBAT);
        setTranslationKey(name);
        setRegistryName(name);
        this.texture = textureName;
        ModItems.ITEMS.add(this);
        this.knockbackResistance = new AttributeModifier("netheriteKnockbackResistance", 0.1, 0);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == this.armorType) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", this.damageReduceAmount, 0));
            multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockbackResistance);
            // Override armor toughness to make is adjustable in game
            //Come back to Re-use when needed IF needed
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", 3.0F, 0));

        }


        return multimap;
    }

    public ArmorBase setArmorBonusDesc(String armorBonusDesc) {
        this.armorBonusDesc = armorBonusDesc;
        return this;
    }

    protected ModelBiped getCustomModel() {
        return null;
    }



    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (this.getCustomModel() != null) {
            return ModReference.MOD_ID + ":textures/models/armor/" + this.texture;
        }

        // Basically the normal string that is generated by minecraft's default armor
        int layer = slot == EntityEquipmentSlot.LEGS ? 2 : 1;
        String t = type == null ? "" : String.format("_%s", type);
        return String.format("%s:textures/models/armor/%s_layer_%d%s.png", ModReference.MOD_ID, this.texture, layer, t);
    }

    /*
     * Sets up a custom armor model
     */
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (this.getCustomModel() != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ArmorBase) {
            ModelBiped model = getCustomModel();
            model.bipedHead.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
            model.bipedBody.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
            model.bipedLeftArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
            model.bipedRightArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
            model.bipedLeftLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET);
            model.bipedRightLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS || armorSlot == EntityEquipmentSlot.FEET);

            model.isChild = _default.isChild;
            model.isRiding = _default.isRiding;
            model.isSneak = _default.isSneak;
            model.rightArmPose = _default.rightArmPose;
            model.leftArmPose = _default.leftArmPose;

            return model;
        }
        return null;
    }

}