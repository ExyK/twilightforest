package twilightforest.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3i;

import javax.annotation.Nullable;

public class StructureBoundingBoxUtils {
	public static Vec3i getCenter(MutableBoundingBox sbb) {
		return new BlockPos(sbb.minX + (sbb.maxX - sbb.minX + 1) / 2, sbb.minY + (sbb.maxY - sbb.minY + 1) / 2, sbb.minZ + (sbb.maxZ - sbb.minZ + 1) / 2);
	}

	@SuppressWarnings("unused")
	@Nullable
	public static MutableBoundingBox getUnionOfSBBs(MutableBoundingBox sbbIn, MutableBoundingBox sbbMask) {
		if (!sbbIn.intersectsWith(sbbMask))
			return null;

		return new MutableBoundingBox(
				sbbIn.minX > sbbMask.minX ? sbbIn.minX : sbbMask.minX,
				sbbIn.minY > sbbMask.minY ? sbbIn.minY : sbbMask.minY,
				sbbIn.minZ > sbbMask.minZ ? sbbIn.minZ : sbbMask.minZ,
				sbbIn.maxX < sbbMask.maxX ? sbbIn.maxX : sbbMask.maxX,
				sbbIn.maxY < sbbMask.maxY ? sbbIn.maxY : sbbMask.maxY,
				sbbIn.maxZ < sbbMask.maxZ ? sbbIn.maxZ : sbbMask.maxZ);
	}

	public static AxisAlignedBB toAABB(MutableBoundingBox sbb) {
		return new AxisAlignedBB(sbb.minX, sbb.minY, sbb.minZ, sbb.maxX + 1, sbb.maxY + 1, sbb.maxZ + 1);
	}
}
