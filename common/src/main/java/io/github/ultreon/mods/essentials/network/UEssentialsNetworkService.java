package io.github.ultreon.mods.essentials.network;

import com.ultreon.mods.lib.network.api.service.NetworkService;

public class UEssentialsNetworkService implements NetworkService {
    @Override
    public void setup() {
        Networking.initialize();
    }
}
