package moe.yukari.lifeaftercalamity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.CountConfig;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import moe.yukari.lifeaftercalamity.tools.CalamityItemMaterial;
import moe.yukari.lifeaftercalamity.tools.CalamityPickaxe;

public class LifeAfterCalamity implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("lifeaftercalamity");

	//Ores
	//我默认Fabric 1.16.5没有矿物生成了
    //public static ConfiguredFeature<?, ?> ORE_ANCIENT_ORE = Feature.ORE
	//    .configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, LifeAfterCalamity.ANCIENT_ORE.getDefaultState(), 2))
	//	.decorate(Decorator.COUNT.configure(new CountConfig(6)));

	//开发者勋章
	//Yukari
	public static final Item DEVELOPER_CERT_YUKARI = new Item(new FabricItemSettings());
	//Mibino
    public static final Item DEVELOPER_CERT_MIBINO = new Item(new FabricItemSettings());
	//minqwq
	public static final Item DEVELOPER_CERT_MINQWQ = new Item(new FabricItemSettings());
	//Xy_Lose
	public static final Item DEVELOPER_CERT_XYLOSE = new Item(new FabricItemSettings());

	//注册物品 Register Items
	public static final Item CALAMITY_CORE = new Item(new FabricItemSettings());  //灾厄核心
	public static final Item STONE_PICKAXE_HEAD = new Item(new FabricItemSettings());  //石镐头
	public static final Item ENDER_CORE = new Item(new FabricItemSettings());  //末影核心
	public static final Item ENDER_POWER_BATTERY = new Item(new FabricItemSettings());  //末影能量电池
	public static final CalamityPickaxe CALAMITY_PICKAXE = new CalamityPickaxe(new CalamityItemMaterial(), 3, -0.8f, new Item.Settings());  //灾厄之镐
	public static final Item ANCIENT_INGOT = new Item(new FabricItemSettings());  //远古合金
	public static final Item FLAMARINE_FORGING_RECIPE = new Item(new FabricItemSettings());   //烈焰锻造材料
	public static final Item IRON_COARSE = new Item(new FabricItemSettings());  //粗铁球
	public static final Item SULPHUR = new Item(new FabricItemSettings());
	public static final Item MADIDIED = new Item(new FabricItemSettings());

	//碎片系列物品
    public static final Item COBBLESTONE_FRAGMENT = new Item(new FabricItemSettings());  //圆石碎片
	public static final Item DIAMOND_FRAGMENT = new Item(new FabricItemSettings());  //钻石碎片
	public static final Item IRON_FRAGMENT = new Item(new FabricItemSettings());  //铁矿碎片

	//当然是方块啦
	public static final Block ANCIENT_ORE = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2));
	public static final Block SULPHUR_ORE = new Block(FabricBlockSettings.of(Material.METAL).strength(4.2f).requiresTool().breakByTool(FabricToolTags.PICKAXES, 1));

	//首先我要揍死fabric wiki
	private static ConfiguredFeature<?, ?> ORE_ANCIENT_OVERWORLD = Feature.ORE
	    .configure(new OreFeatureConfig(
			OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
			LifeAfterCalamity.ANCIENT_ORE.getDefaultState(),
		3))
		.rangeOf(40)
		.spreadHorizontally()
		.repeat(15);
	
	private static ConfiguredFeature<?, ?> ORE_SULPHUR_OVERWORLD = Feature.ORE
	    .configure(new OreFeatureConfig(
			OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
			LifeAfterCalamity.SULPHUR_ORE.getDefaultState(),
		8))
		.rangeOf(11)
		.spreadHorizontally()
		.repeat(5);

	@Override
	public void onInitialize() {

		LOGGER.info("[LifeAfterCalamity] 世界核心已加载！ World Core is Loaded!");

		//注册物品第二部分 Register Items Part 2
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "calamity_core"), CALAMITY_CORE);
        Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "stone_pickaxe_head"), STONE_PICKAXE_HEAD);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "ender_core"), ENDER_CORE);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "ender_power_battery"), ENDER_POWER_BATTERY);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "calamity_pickaxe"), CALAMITY_PICKAXE);
	    Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "ancient_ingot"), ANCIENT_INGOT);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "flamarine_forging_recipe"), FLAMARINE_FORGING_RECIPE);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "cobblestone_fragment"), COBBLESTONE_FRAGMENT);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "diamond_fragment"), DIAMOND_FRAGMENT);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "iron_coarse"), IRON_COARSE);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "iron_fragment"), IRON_FRAGMENT);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "sulphur"), SULPHUR);
        Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "madidied"), MADIDIED);

		//开发者勋章
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "developer_cert_yukari"), DEVELOPER_CERT_YUKARI);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "developer_cert_mibino"), DEVELOPER_CERT_MIBINO);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "developer_cert_minqwq"), DEVELOPER_CERT_MINQWQ);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "developer_cert_xylose"), DEVELOPER_CERT_XYLOSE);

		//当然是方块啦
		Registry.register(Registry.BLOCK, new Identifier("lifeaftercalamity", "ancient_ore"), ANCIENT_ORE);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "ancient_ore"), new BlockItem(ANCIENT_ORE, new FabricItemSettings()));

		Registry.register(Registry.BLOCK, new Identifier("lifeaftercalamity", "sulphur_ore"), SULPHUR_ORE);
		Registry.register(Registry.ITEM, new Identifier("lifeaftercalamity", "sulphur_ore"), new BlockItem(SULPHUR_ORE, new FabricItemSettings()));

		//Ores!
		RegistryKey<ConfiguredFeature<?, ?>> oreAncientOW = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("lifeaftercalamity", "ancient_ore_overworld"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreAncientOW.getValue(), ORE_ANCIENT_OVERWORLD);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreAncientOW);
		
		RegistryKey<ConfiguredFeature<?, ?>> oreSulphurOW = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("lifeaftercalamity", "sulphur_ore_overworld"));
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, oreSulphurOW.getValue(), ORE_SULPHUR_OVERWORLD);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, oreSulphurOW);

		//会修的可以在这帮我修一下（
        //ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);

	}

    //普通方块物品组
	public static final ItemGroup CALAMITY_GROUP = FabricItemGroupBuilder.create(
		new Identifier("lifeaftercalamity", "calamity"))
		.icon(() -> new ItemStack(LifeAfterCalamity.CALAMITY_CORE))
		.appendItems(stacks -> {
			stacks.add(new ItemStack(LifeAfterCalamity.CALAMITY_CORE));
			stacks.add(new ItemStack(LifeAfterCalamity.CALAMITY_PICKAXE));
			stacks.add(new ItemStack(LifeAfterCalamity.STONE_PICKAXE_HEAD));
			stacks.add(new ItemStack(LifeAfterCalamity.ENDER_CORE));
			stacks.add(new ItemStack(LifeAfterCalamity.ENDER_POWER_BATTERY));
			stacks.add(new ItemStack(LifeAfterCalamity.ANCIENT_INGOT));
			stacks.add(new ItemStack(LifeAfterCalamity.FLAMARINE_FORGING_RECIPE));
			stacks.add(new ItemStack(LifeAfterCalamity.COBBLESTONE_FRAGMENT));
			stacks.add(new ItemStack(LifeAfterCalamity.DIAMOND_FRAGMENT));
			stacks.add(new ItemStack(LifeAfterCalamity.IRON_COARSE));
			stacks.add(new ItemStack(LifeAfterCalamity.IRON_FRAGMENT));
			stacks.add(new ItemStack(LifeAfterCalamity.ANCIENT_ORE));
			stacks.add(new ItemStack(LifeAfterCalamity.SULPHUR_ORE));
			stacks.add(new ItemStack(LifeAfterCalamity.SULPHUR));
		})
	.build();

	//开发者勋章物品组
	public static final ItemGroup DEVELOPERS_CALAMITY_GROUP = FabricItemGroupBuilder.create(
		new Identifier("lifeaftercalamity", "developers_calamity"))
	    .icon(() -> new ItemStack(LifeAfterCalamity.DEVELOPER_CERT_YUKARI))
		.appendItems(stacks -> {
            stacks.add(new ItemStack(LifeAfterCalamity.DEVELOPER_CERT_YUKARI));
			stacks.add(new ItemStack(LifeAfterCalamity.DEVELOPER_CERT_MIBINO));
			stacks.add(new ItemStack(LifeAfterCalamity.DEVELOPER_CERT_MINQWQ));
			stacks.add(new ItemStack(LifeAfterCalamity.DEVELOPER_CERT_XYLOSE));
		})
	.build();

	/*
	private void onServerTick(ServerWorld world) {
		for (ServerPlayerEntity player : world.getPlayers()) {
			StatusEffectInstance miningFatigue = new StatusEffectInstance(StatusEffects.MINING_FATIGUE, Integer.MAX_VALUE, 0, false, false);
			player.addStatusEffect(miningFatigue);

			if (player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue() != 12.0) {
				player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(12.0);
				if (player.getHealth() > 12.0f) {
					player.setHealth(12.0f);
				}
			}
		}
	}
	*/
}