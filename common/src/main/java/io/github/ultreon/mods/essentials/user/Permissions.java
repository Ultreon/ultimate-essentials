package io.github.ultreon.mods.essentials.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Permissions {
    private static final Set<Permission> PERMISSIONS = new HashSet<>();

    // Interactions
    public static final Permission BREAK_BLOCKS = create(new Permission("world.blocks.break", true));
    public static final Permission INTERACT_BLOCKS = create(new Permission("world.blocks.interact", true));
    public static final Permission ATTACK_ENTITIES = create(new Permission("entities.attack", true));
    public static final Permission INTERACT_ENTITIES = create(new Permission("entities.interact", true));
    public static final Permission USE_ITEM = create(new Permission("items.use", true));
    public static final Permission EAT_ITEM = create(new Permission("items.eat", true));
    public static final Permission DROP_ITEM = create(new Permission("items.drop", true));
    public static final Permission CHAT = create(new Permission("chat", true));
    public static final Permission CHAT_MENTION = create(new Permission("chat.mention", true));
    public static final Permission CHAT_FORMATTING = create(new Permission("chat.formatting"));

    // Permissions
    public static final Permission CHANGE_PERMISSIONS = create(new Permission("permissions.change"));
    public static final Permission CHANGE_USER_PERMISSIONS = create(new Permission("permissions.change.user"));
    public static final Permission CHANGE_ROLE_PERMISSIONS = create(new Permission("permissions.change.role"));

    // Warps
    public static final Permission CREATE_WARPS = create(new Permission("warps.create"));
    public static final Permission RENAME_WARPS = create(new Permission("warps.correct.name"));
    public static final Permission MODIFY_WARPS = create(new Permission("warps.modify"));
    public static final Permission DELETE_WARPS = create(new Permission("warps.modify.delete"));
    public static final Permission REPOSITION_WARPS = create(new Permission("warps.modify.position"));
    public static final Permission SET_ROTATION_WARPS = create(new Permission("warps.modify.rotation"));

    // Roles
    public static final Permission CREATE_ROLES = create(new Permission("roles.create"));
    public static final Permission DELETE_ROLES = create(new Permission("roles.delete"));
    public static final Permission MODIFY_ROLES = create(new Permission("roles.modify"));
    public static final Permission ASSIGN_ROLES = create(new Permission("roles.assign"));

    // Moderation
    public static final Permission TELEPORT = create(new Permission("moderation.tp"));
    public static final Permission TELEPORT_OTHERS = create(new Permission("moderation.tp.others"));
    public static final Permission TELEPORT_ME = create(new Permission("moderation.tp.me"));

    public static final Permission KICK_PLAYERS = create(new Permission("moderation.kick"));
    public static final Permission BAN_PLAYERS = create(new Permission("moderation.ban"));
    public static final Permission DEOP_PLAYERS = create(new Permission("moderation.deop"));
    public static final Permission OP_PLAYERS = create(new Permission("moderation.op"));

    public static final Permission INSTA_KILL = create(new Permission("moderation.insta_kill"));

    // Cheats
    public static final Permission CHEATS_FULL_FEED = create(new Permission("cheats.full_feed"));
    public static final Permission CHEATS_FULL_HEAL = create(new Permission("cheats.full_heal"));
    public static final Permission CHEATS_BURN = create(new Permission("cheats.burn"));

    public static final Permission WEATHER_SET = create(new Permission("weather.set"));
    public static final Permission TIME_SET = create(new Permission("time.set"));

    public static final Permission GOD_MODE = create(new Permission("modes.god"));
    public static final Permission FLY_MODE = create(new Permission("modes.fly"));
    public static final Permission VANISH = create(new Permission("modes.vanish"));

    // Accounts
    public static final Permission MANAGE_ACCOUNTS = create(new Permission("accounts"));
    public static final Permission CREATE_ACCOUNTS = create(new Permission("accounts.create"));
    public static final Permission DELETE_ACCOUNTS = create(new Permission("accounts.delete"));
    public static final Permission MODIFY_ACCOUNTS = create(new Permission("accounts.modify"));

    // Invites
    public static final Permission MANAGE_INVITES = create(new Permission("invites"));
    public static final Permission CREATE_INVITES = create(new Permission("invites.create"));
    public static final Permission REMOVE_INVITES = create(new Permission("invites.remove"));

    public static final Permission MASTER = create(new Permission("*"));

    private static Permission create(Permission permission) {
        PERMISSIONS.add(permission);
        return permission;
    }

    private static void ctxCr(String ctxS) {

    }

    public static void initClass() {

    }

    public static Set<Permission> getPerms() {
        return Collections.unmodifiableSet(PERMISSIONS);
    }
}
