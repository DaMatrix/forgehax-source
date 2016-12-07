/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IWorldEventListener
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 */
package com.matt.forgehax.events.listeners;

import com.matt.forgehax.events.EntityAddedEvent;
import com.matt.forgehax.events.EntityRemovedEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class WorldListener implements IWorldEventListener {
	@Override
	public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
	}
	@Override
	public void notifyLightSet(BlockPos pos) {
	}
	@Override
	public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
	}
	@Override
	public void playSoundToAllNearExcept(EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y,
			double z, float volume, float pitch) {
	}
	@Override
	public void playRecord(SoundEvent soundIn, BlockPos pos) {
	}
	@Override
	public /* varargs */ void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord,
			double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
	}
	@Override
	public void onEntityAdded(Entity entityIn) {
		MinecraftForge.EVENT_BUS.post((Event) new EntityAddedEvent(entityIn));
	}
	@Override
	public void onEntityRemoved(Entity entityIn) {
		MinecraftForge.EVENT_BUS.post((Event) new EntityRemovedEvent(entityIn));
	}
	@Override
	public void broadcastSound(int soundID, BlockPos pos, int data) {
	}
	@Override
	public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
	}
	@Override
	public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
	}
	@Override
	public void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, double p_190570_4_,
			double p_190570_6_, double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_,
			int... p_190570_16_) {		
	}
}
