package io.github.ultreon.mods.essentials.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiPlayerGameMode.class)
public abstract class PlayerControllerMixin {
//    @Inject(method = "attackEntity(Lnet/minecraft/entity/player/Player;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
//    public void attackEntity(Player p_78764_1_, Entity p_78764_2_, CallbackInfo ci) {
////        if (ChallengeManager.client.isEnabled(ModChallenges.NO_ATTACK) && p_78764_2_ instanceof LivingEntity) {
////            ci.cancel();
////        }
//    }
//
//    @Inject(method = "clickBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)Z", at = @At("HEAD"), cancellable = true)
//    public void clickBlock(BlockPos pos, Direction blockstate1, CallbackInfoReturnable<Boolean> cir) {
//        if (Minecraft.getInstance().player != null) {
//            ClaimedChunkManager manager = FTBChunksAPI.getManager();
//            ClaimedChunk chunk = FTBChunksAPI.getManager().getChunk(new ChunkDimPos(Minecraft.getInstance().player.world, pos));
//
//            {
//
//                cir.setReturnValue(false);
//            }
//        }
//    }
//
//    @Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)Z", at = @At("HEAD"), cancellable = true)
//    public void onPlayerDamageBlock(BlockPos posBlock, Direction directionFacing, CallbackInfoReturnable<Boolean> cir) {
////        if (ChallengeManager.client.isEnabled(ModChallenges.NO_BLOCK_BREAKING)) {
////            cir.setReturnValue(false);
////        }
//    }
}
