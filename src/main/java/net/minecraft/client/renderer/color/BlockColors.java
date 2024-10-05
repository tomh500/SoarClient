package net.minecraft.client.renderer.color;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockDoublePlant.EnumBlockHalf;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import org.jetbrains.annotations.Nullable;

import com.soarclient.libraries.sodium.client.world.biome.BlockColorsExtended;

public class BlockColors implements BlockColorsExtended {
	private final ObjectIntIdentityMap<IBlockColor> mapBlockColors = new ObjectIntIdentityMap();
	private final Reference2ReferenceMap<Block, IBlockColor> blocksToColor = new Reference2ReferenceOpenHashMap<>();
	private static final IBlockColor DEFAULT_PROVIDER = (state, view, pos, tint) -> -1;
	public static BlockColors INSTANCE;

	public BlockColors() {
		this.blocksToColor.defaultReturnValue(DEFAULT_PROVIDER);
	}

	public static BlockColors init() {
		INSTANCE = new BlockColors();
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			EnumPlantType type = (EnumPlantType) state.getValue(BlockDoublePlant.VARIANT);
			return worldIn != null && pos != null && (type == EnumPlantType.GRASS || type == EnumPlantType.FERN)
					? BiomeColorHelper.getGrassColorAtPos(worldIn,
							state.getValue(BlockDoublePlant.HALF) == EnumBlockHalf.UPPER ? pos.down() : pos)
					: -1;
		}, Blocks.double_plant);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (worldIn != null && pos != null) {
				TileEntity tileentity = worldIn.getTileEntity(pos);
				if (tileentity instanceof TileEntityFlowerPot) {
					Item item = ((TileEntityFlowerPot) tileentity).getFlowerPotItem();
					IBlockState iblockstate = Block.getBlockFromItem(item).getDefaultState();
					return INSTANCE.colorMultiplier(iblockstate, worldIn, pos, tintIndex);
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		}, Blocks.flower_pot);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null
				? BiomeColorHelper.getGrassColorAtPos(worldIn, pos)
				: ColorizerGrass.getGrassColor(0.5, 1.0), Blocks.grass);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			EnumType blockplanks$enumtype = (EnumType) state.getValue(BlockOldLeaf.VARIANT);
			if (blockplanks$enumtype == EnumType.SPRUCE) {
				return ColorizerFoliage.getFoliageColorPine();
			} else if (blockplanks$enumtype == EnumType.BIRCH) {
				return ColorizerFoliage.getFoliageColorBirch();
			} else {
				return worldIn != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos)
						: ColorizerFoliage.getFoliageColorBasic();
			}
		}, Blocks.leaves);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null
				? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos)
				: ColorizerFoliage.getFoliageColorBasic(), Blocks.leaves2);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos,
				tintIndex) -> worldIn != null && pos != null ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : -1,
				Blocks.water, Blocks.flowing_water);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos,
				tintIndex) -> worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : -1,
				Blocks.reeds);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			int i = (Integer) state.getValue(BlockStem.AGE);
			int j = i * 32;
			int k = 255 - i * 8;
			int l = i * 4;
			return j << 16 | k << 8 | l;
		}, Blocks.melon_stem, Blocks.pumpkin_stem);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (worldIn != null && pos != null) {
				return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
			} else {
				return state.getValue(BlockTallGrass.TYPE) == net.minecraft.block.BlockTallGrass.EnumType.DEAD_BUSH
						? 16777215
						: ColorizerGrass.getGrassColor(0.5, 1.0);
			}
		}, Blocks.tallgrass);
		INSTANCE.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null
				? BiomeColorHelper.getFoliageColorAtPos(worldIn, pos)
				: ColorizerFoliage.getFoliageColorBasic(), Blocks.vine);
		INSTANCE.registerBlockColorHandler(
				(state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? 2129968 : 7455580,
				Blocks.waterlily);
		return INSTANCE;
	}

	public int getColor(IBlockState p_189991_1_, World p_189991_2_, BlockPos p_189991_3_) {
		IBlockColor iblockcolor = (IBlockColor) this.mapBlockColors
				.getByValue(Block.getIdFromBlock(p_189991_1_.getBlock()));
		if (iblockcolor != null) {
			return iblockcolor.colorMultiplier(p_189991_1_, null, null, 0);
		} else {
			MapColor mapcolor = p_189991_1_.getBlock().getMapColor(p_189991_1_);
			return mapcolor != null ? mapcolor.colorValue : -1;
		}
	}

	public int colorMultiplier(IBlockState state, @Nullable IBlockAccess blockAccess, @Nullable BlockPos pos,
			int renderPass) {
		IBlockColor iblockcolor = (IBlockColor) this.mapBlockColors.getByValue(Block.getIdFromBlock(state.getBlock()));
		return iblockcolor == null ? -1 : iblockcolor.colorMultiplier(state, blockAccess, pos, renderPass);
	}

	public void registerBlockColorHandler(IBlockColor blockColor, Block... blocksIn) {
		synchronized (this.blocksToColor) {
			for (Block block : blocksIn) {
				if (blockColor != null) {
					this.blocksToColor.put(block, blockColor);
				}
			}
		}

		for (Block blockx : blocksIn) {
			this.mapBlockColors.put(blockColor, Block.getIdFromBlock(blockx));
		}
	}

	@Override
	public IBlockColor getColorProvider(IBlockState state) {
		return this.blocksToColor.get(state.getBlock());
	}

	static {
		init();
	}
}
