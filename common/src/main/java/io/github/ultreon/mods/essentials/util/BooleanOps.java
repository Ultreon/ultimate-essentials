package io.github.ultreon.mods.essentials.util;

import java.util.function.BooleanSupplier;

public interface BooleanOps extends BooleanSupplier {
    default BooleanOps and(BooleanSupplier sup) {
        return () -> getAsBoolean() && sup.getAsBoolean();
    }

    default BooleanOps or(BooleanSupplier sup) {
        return () -> getAsBoolean() || sup.getAsBoolean();
    }

    default BooleanOps xor(BooleanSupplier sup) {
        return () -> getAsBoolean() ^ sup.getAsBoolean();
    }

    default BooleanOps nand(BooleanSupplier sup) {
        return () -> !(getAsBoolean() && sup.getAsBoolean());
    }

    default BooleanOps nor(BooleanSupplier sup) {
        return () -> !(getAsBoolean() || sup.getAsBoolean());
    }

    @SuppressWarnings("SimplifiableBooleanExpression")
    default BooleanOps xnor(BooleanSupplier sup) {
        return () -> !(getAsBoolean() ^ sup.getAsBoolean());
    }

    default BooleanOps eq(BooleanSupplier sup) {
        return () -> getAsBoolean() == sup.getAsBoolean();
    }

    default BooleanOps neq(BooleanSupplier sup) {
        return () -> getAsBoolean() != sup.getAsBoolean();
    }

    default BooleanOps not() {
        return () -> !getAsBoolean();
    }
}
