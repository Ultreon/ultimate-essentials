package io.github.ultreon.mods.essentials.mixin;

import net.minecraft.client.server.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin {
//    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
//    public void sendMessage(Component message, UUID senderUUID, CallbackInfo ci) {
//        ServerPlayer playerByUUID = getPlayerList().getPlayer(senderUUID);
//        boolean containsVirus = VirusScanner.get().scan(message);
//        if (containsVirus) {
//            if (playerByUUID != null) {
//                ServerGamePacketListenerImpl connection = playerByUUID.connection;
//                if (connection != null) {
//                    connection.disconnect(new Component("Using exploits to hack the server is disallowed."));
//                }
//            }
//            ci.cancel();
//        }
//    }
}
