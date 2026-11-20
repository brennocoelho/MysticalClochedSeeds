
package com.doncoelho.mysticalclochedseeds.crux;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;

public class CruxedFarmlandBlock extends FarmBlock {

    public CruxedFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    // Prevent farmland from being trampled by entities.
    @Override
    public void fallOn(
            Level level,
            BlockState state,
            BlockPos pos,
            Entity entity,
            float fallDistance
    ) {
        entity.causeFallDamage(
                fallDistance,
                1.0F,
                level.damageSources().fall()
        );
    }

    // Preserve the farmland block when it becomes dry.
    @Override
    public void randomTick(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        int moisture = state.getValue(MOISTURE);

        if (!hasNearbyWater(level, pos)
                && !level.isRainingAt(pos.above())) {

            if (moisture > 0) {
                level.setBlock(
                        pos,
                        state.setValue(MOISTURE, moisture - 1),
                        2
                );
            }

        } else if (moisture < 7) {
            level.setBlock(
                    pos,
                    state.setValue(MOISTURE, 7),
                    2
            );
        }
    }

    @Override
    protected boolean canSurvive(
            BlockState state,
            LevelReader level,
            BlockPos pos
    ) {
        // Cruxed Farmland must never turn into regular Dirt
        // because of blocks placed above it.
        return true;
    }

    private static boolean hasNearbyWater(
            LevelReader level,
            BlockPos pos
    ) {
        BlockPos start = pos.offset(-4, 0, -4);
        BlockPos end = pos.offset(4, 1, 4);

        for (BlockPos checkPos : BlockPos.betweenClosed(start, end)) {
            if (level.getFluidState(checkPos).is(FluidTags.WATER)) {
                return true;
            }
        }

        return false;
    }
}
