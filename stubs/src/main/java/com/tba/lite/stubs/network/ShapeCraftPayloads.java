package com.tba.lite.stubs.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub payload registrations for ShapeCraft (namespace: shapecraft).
 * 9 payloads: 3 C2S + 6 S2C. Mirrors ShapeCraft v0.4.18 wire format.
 */
public final class ShapeCraftPayloads {

    private static final String NS = "shapecraft";

    private ShapeCraftPayloads() {}

    // ── C2S ──────────────────────────────────────────────────────────────

    public record HandshakeC2S(String modVersion, int protocolVersion) implements CustomPacketPayload {
        public static final Type<HandshakeC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "handshake"));
        public static final StreamCodec<RegistryFriendlyByteBuf, HandshakeC2S> CODEC =
                StreamCodec.of(HandshakeC2S::write, HandshakeC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, HandshakeC2S p) {
            buf.writeUtf(p.modVersion); buf.writeInt(p.protocolVersion);
        }
        private static HandshakeC2S read(RegistryFriendlyByteBuf buf) {
            return new HandshakeC2S(buf.readUtf(), buf.readInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record GenerateRequestC2S(String description) implements CustomPacketPayload {
        public static final Type<GenerateRequestC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "generate_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GenerateRequestC2S> CODEC =
                StreamCodec.of(GenerateRequestC2S::write, GenerateRequestC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, GenerateRequestC2S p) {
            buf.writeUtf(p.description);
        }
        private static GenerateRequestC2S read(RegistryFriendlyByteBuf buf) {
            return new GenerateRequestC2S(buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record BlockSyncRequestC2S() implements CustomPacketPayload {
        public static final Type<BlockSyncRequestC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "block_sync_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BlockSyncRequestC2S> CODEC =
                StreamCodec.of(BlockSyncRequestC2S::write, BlockSyncRequestC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, BlockSyncRequestC2S p) {}
        private static BlockSyncRequestC2S read(RegistryFriendlyByteBuf buf) {
            return new BlockSyncRequestC2S();
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── S2C ──────────────────────────────────────────────────────────────

    public record HandshakeResponseS2C(boolean success, int protocolVersion,
                                       String message, int poolSize) implements CustomPacketPayload {
        public static final Type<HandshakeResponseS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "handshake_response"));
        public static final StreamCodec<RegistryFriendlyByteBuf, HandshakeResponseS2C> CODEC =
                StreamCodec.of(HandshakeResponseS2C::write, HandshakeResponseS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, HandshakeResponseS2C p) {
            buf.writeBoolean(p.success);
            buf.writeInt(p.protocolVersion);
            buf.writeUtf(p.message != null ? p.message : "");
            buf.writeInt(p.poolSize);
        }
        private static HandshakeResponseS2C read(RegistryFriendlyByteBuf buf) {
            return new HandshakeResponseS2C(buf.readBoolean(), buf.readInt(),
                    buf.readUtf(), buf.readInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record GenerationStatusS2C(int status, String message) implements CustomPacketPayload {
        public static final Type<GenerationStatusS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "generation_status"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GenerationStatusS2C> CODEC =
                StreamCodec.of(GenerationStatusS2C::write, GenerationStatusS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, GenerationStatusS2C p) {
            buf.writeInt(p.status); buf.writeUtf(p.message);
        }
        private static GenerationStatusS2C read(RegistryFriendlyByteBuf buf) {
            return new GenerationStatusS2C(buf.readInt(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record GenerationCompleteS2C(int slotIndex, String displayName, String modelJson,
                                        String upperModelJson, String modelJsonOpen,
                                        String upperModelJsonOpen, String blockType,
                                        String textureTints) implements CustomPacketPayload {
        public static final Type<GenerationCompleteS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "generation_complete"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GenerationCompleteS2C> CODEC =
                StreamCodec.of(GenerationCompleteS2C::write, GenerationCompleteS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, GenerationCompleteS2C p) {
            buf.writeInt(p.slotIndex);
            buf.writeUtf(p.displayName);
            buf.writeUtf(p.modelJson);
            buf.writeUtf(p.upperModelJson != null ? p.upperModelJson : "");
            buf.writeUtf(p.modelJsonOpen != null ? p.modelJsonOpen : "");
            buf.writeUtf(p.upperModelJsonOpen != null ? p.upperModelJsonOpen : "");
            buf.writeUtf(p.blockType != null ? p.blockType : "");
            buf.writeUtf(p.textureTints != null ? p.textureTints : "");
        }
        private static GenerationCompleteS2C read(RegistryFriendlyByteBuf buf) {
            return new GenerationCompleteS2C(buf.readInt(), buf.readUtf(), buf.readUtf(),
                    buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record GenerationErrorS2C(String message, String errorCode) implements CustomPacketPayload {
        public static final Type<GenerationErrorS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "generation_error"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GenerationErrorS2C> CODEC =
                StreamCodec.of(GenerationErrorS2C::write, GenerationErrorS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, GenerationErrorS2C p) {
            buf.writeUtf(p.message); buf.writeUtf(p.errorCode);
        }
        private static GenerationErrorS2C read(RegistryFriendlyByteBuf buf) {
            return new GenerationErrorS2C(buf.readUtf(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record BlockSyncS2C(List<BlockSyncEntry> entries) implements CustomPacketPayload {
        public record BlockSyncEntry(int slotIndex, String displayName, String modelJson,
                                     String upperModelJson, String modelJsonOpen,
                                     String upperModelJsonOpen, String blockType,
                                     String textureTints) {}

        public static final Type<BlockSyncS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "block_sync"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BlockSyncS2C> CODEC =
                StreamCodec.of(BlockSyncS2C::write, BlockSyncS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, BlockSyncS2C p) {
            buf.writeInt(p.entries.size());
            for (BlockSyncEntry e : p.entries) {
                buf.writeInt(e.slotIndex);
                buf.writeUtf(e.displayName);
                buf.writeUtf(e.modelJson);
                buf.writeUtf(e.upperModelJson != null ? e.upperModelJson : "");
                buf.writeUtf(e.modelJsonOpen != null ? e.modelJsonOpen : "");
                buf.writeUtf(e.upperModelJsonOpen != null ? e.upperModelJsonOpen : "");
                buf.writeUtf(e.blockType != null ? e.blockType : "");
                buf.writeUtf(e.textureTints != null ? e.textureTints : "");
            }
        }
        private static BlockSyncS2C read(RegistryFriendlyByteBuf buf) {
            int count = buf.readInt();
            List<BlockSyncEntry> entries = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                entries.add(new BlockSyncEntry(
                        buf.readInt(), buf.readUtf(), buf.readUtf(), buf.readUtf(),
                        buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readUtf()));
            }
            return new BlockSyncS2C(entries);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record DoorDebugSyncS2C(int textureClosedOffset, int textureOpenOffset,
                                   int hitboxClosedOffset, int hitboxOpenOffset) implements CustomPacketPayload {
        public static final Type<DoorDebugSyncS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "door_debug_sync"));
        public static final StreamCodec<RegistryFriendlyByteBuf, DoorDebugSyncS2C> CODEC =
                StreamCodec.of(DoorDebugSyncS2C::write, DoorDebugSyncS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, DoorDebugSyncS2C p) {
            buf.writeInt(p.textureClosedOffset); buf.writeInt(p.textureOpenOffset);
            buf.writeInt(p.hitboxClosedOffset); buf.writeInt(p.hitboxOpenOffset);
        }
        private static DoorDebugSyncS2C read(RegistryFriendlyByteBuf buf) {
            return new DoorDebugSyncS2C(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── Registration ─────────────────────────────────────────────────────

    public static void register() {
        // C2S
        PayloadTypeRegistry.playC2S().register(HandshakeC2S.TYPE, HandshakeC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(GenerateRequestC2S.TYPE, GenerateRequestC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(BlockSyncRequestC2S.TYPE, BlockSyncRequestC2S.CODEC);

        // S2C
        PayloadTypeRegistry.playS2C().register(HandshakeResponseS2C.TYPE, HandshakeResponseS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(GenerationStatusS2C.TYPE, GenerationStatusS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(GenerationCompleteS2C.TYPE, GenerationCompleteS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(GenerationErrorS2C.TYPE, GenerationErrorS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(BlockSyncS2C.TYPE, BlockSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(DoorDebugSyncS2C.TYPE, DoorDebugSyncS2C.CODEC);
    }
}
