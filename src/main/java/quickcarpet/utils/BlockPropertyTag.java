package quickcarpet.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BlockPropertyTag implements Tag.Identified<Block> {
    public final Identifier id;
    private final Predicate<BlockState> property;

    public BlockPropertyTag(Identifier id, Predicate<BlockState> property) {
        this.id = id;
        this.property = property;
    }

    public BlockPropertyTag(Identifier id, BlockPropertyPredicate function) {
        this.id = id;
        this.property = state -> function.test(state, new SingleBlockView(state), BlockPos.ORIGIN);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public boolean contains(Block block) {
        return this.property.test(block.getDefaultState());
    }

    @Override
    public List<Block> values() {
        return Registry.BLOCK.stream().filter(this::contains).collect(Collectors.toList());
    }

    @FunctionalInterface
    interface BlockPropertyPredicate {
        boolean test(BlockState state, BlockView world, BlockPos pos);
    }

    public static class SingleBlockView implements BlockView {
        private final BlockState state;
        public SingleBlockView(BlockState state) {
            this.state = state;
        }

        @Nullable
        @Override
        public BlockEntity getBlockEntity(BlockPos pos) {
            return null;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return state;
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return state.getFluidState();
        }

        @Override
        public int getHeight() {
            return 1;
        }

        @Override
        public int getBottomY() {
            return 0;
        }
    }
}
