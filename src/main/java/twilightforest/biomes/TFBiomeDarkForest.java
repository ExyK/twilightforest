package twilightforest.biomes;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import twilightforest.TFAchievementPage;
import twilightforest.TFFeature;
import twilightforest.entity.EntityTFKingSpider;
import twilightforest.entity.EntityTFKobold;
import twilightforest.entity.EntityTFMistWolf;
import twilightforest.entity.EntityTFSkeletonDruid;



public class TFBiomeDarkForest extends TFBiomeBase {
	
	private static final int MONSTER_SPAWN_RATE = 20;
	private Random monsterRNG;

	public TFBiomeDarkForest(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().canopyPerChunk = 5.5F;
		
		getTFBiomeDecorator().setTreesPerChunk(10);
		getTFBiomeDecorator().setGrassPerChunk(-99);
		getTFBiomeDecorator().setFlowersPerChunk(-99);
        getTFBiomeDecorator().setMushroomsPerChunk(2);
        getTFBiomeDecorator().setDeadBushPerChunk(10);

        
        this.rootHeight = 0.05F;
        this.heightVariation = 0.05F;
        
        this.monsterRNG = new Random();
        
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 5, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 5, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTFMistWolf.class, 10, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTFSkeletonDruid.class, 10, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTFKingSpider.class, 10, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityTFKobold.class, 10, 4, 8));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityWitch.class, 1, 1, 1));
        
        this.theBiomeDecorator.generateLakes = false;
	}
	
	@Override
    public BiomeDecorator createBiomeDecorator()
    {
        return new TFDarkForestBiomeDecorator();
    }
	
    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random)
    {
        if(random.nextInt(5) == 0)
        {
        	return new WorldGenShrub(3, 0);
        }
        if(random.nextInt(8) == 0)
        {
            return this.birchGen;
        }
        else {
            return worldGeneratorTrees;
        }
    }

    @Override
    public int getBiomeGrassColor(int x, int y, int z)
    {
        double var1 = (double)MathHelper.clamp_float(this.getFloatTemperature(x, y, z), 0.0F, 1.0F);
        double var3 = (double)MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
        return ((ColorizerGrass.getGrassColor(var1, var3) & 0xFEFEFE) + 0x1E0E4E) / 2;
        
//        return 0x554114;
    }

    @Override
    public int getBiomeFoliageColor(int x, int y, int z)
    {
        double var1 = (double)MathHelper.clamp_float(this.getFloatTemperature(x, y, z), 0.0F, 1.0F);
        double var3 = (double)MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
        return ((ColorizerFoliage.getFoliageColor(var1, var3) & 0xFEFEFE) + 0x1E0E4E) / 2;
    }
    
    
	@Override
    public List<SpawnListEntry> getSpawnableList(EnumCreatureType par1EnumCreatureType)
    {
    	// if is is monster, then only give it the real list 1/MONSTER_SPAWN_RATE of the time
    	if (par1EnumCreatureType == EnumCreatureType.MONSTER) {
			return monsterRNG.nextInt(MONSTER_SPAWN_RATE) == 0 ? this.spawnableMonsterList : ImmutableList.<SpawnListEntry>of();
    	}
    	else {
    		return par1EnumCreatureType == EnumCreatureType.CREATURE ? this.spawnableCreatureList : (par1EnumCreatureType == EnumCreatureType.WATER_CREATURE ? this.spawnableWaterCreatureList : (par1EnumCreatureType == EnumCreatureType.AMBIENT ? this.spawnableCaveCreatureList : null));
    	}
    }

	@Override
	public boolean isHighHumidity() {
		return true;
	}

    @Override
	protected Achievement getRequiredAchievement() {
		return TFAchievementPage.twilightProgressHydra;
	}

	@Override
	public void enforceProgession(EntityPlayer player, World world) {
		if (!world.isRemote && world.getWorldTime() % 60 == 0) {
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0));
			
			// hint monster?
			if (world.rand.nextInt(4) == 0) {
				TFFeature.tfStronghold.trySpawnHintMonster(world, player);
			}
		}
	}
}