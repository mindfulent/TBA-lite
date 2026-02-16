package com.tba.lite.stubs.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Stub payload registrations for SynthCraft (namespace: synthcraft).
 * 12 payloads: 4 C2S + 8 S2C.
 */
public final class SynthCraftPayloads {

    private static final String NS = "synthcraft";

    private SynthCraftPayloads() {}

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

    public record GenerateRequestC2S(String prompt, String lyrics,
                                      int duration, int inferenceSteps) implements CustomPacketPayload {
        public static final Type<GenerateRequestC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "generate_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GenerateRequestC2S> CODEC =
                StreamCodec.of(GenerateRequestC2S::write, GenerateRequestC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, GenerateRequestC2S p) {
            buf.writeUtf(p.prompt); buf.writeUtf(p.lyrics);
            buf.writeInt(p.duration); buf.writeInt(p.inferenceSteps);
        }
        private static GenerateRequestC2S read(RegistryFriendlyByteBuf buf) {
            return new GenerateRequestC2S(buf.readUtf(), buf.readUtf(), buf.readInt(), buf.readInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record BroadcastRequestC2S(String songId, String audioUrl,
                                       int duration, String prompt) implements CustomPacketPayload {
        public static final Type<BroadcastRequestC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "broadcast_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BroadcastRequestC2S> CODEC =
                StreamCodec.of(BroadcastRequestC2S::write, BroadcastRequestC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, BroadcastRequestC2S p) {
            buf.writeUtf(p.songId); buf.writeUtf(p.audioUrl);
            buf.writeInt(p.duration); buf.writeUtf(p.prompt);
        }
        private static BroadcastRequestC2S read(RegistryFriendlyByteBuf buf) {
            return new BroadcastRequestC2S(buf.readUtf(), buf.readUtf(), buf.readInt(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record PlaybackActionC2S(String action) implements CustomPacketPayload {
        public static final Type<PlaybackActionC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "playback_action"));
        public static final StreamCodec<RegistryFriendlyByteBuf, PlaybackActionC2S> CODEC =
                StreamCodec.of(PlaybackActionC2S::write, PlaybackActionC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, PlaybackActionC2S p) {
            buf.writeUtf(p.action);
        }
        private static PlaybackActionC2S read(RegistryFriendlyByteBuf buf) {
            return new PlaybackActionC2S(buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── S2C ──────────────────────────────────────────────────────────────

    public record HandshakeResponseS2C(String serverModVersion, int protocolVersion,
                                        boolean licenseValid, String message) implements CustomPacketPayload {
        public static final Type<HandshakeResponseS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "handshake_response"));
        public static final StreamCodec<RegistryFriendlyByteBuf, HandshakeResponseS2C> CODEC =
                StreamCodec.of(HandshakeResponseS2C::write, HandshakeResponseS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, HandshakeResponseS2C p) {
            buf.writeUtf(p.serverModVersion); buf.writeInt(p.protocolVersion);
            buf.writeBoolean(p.licenseValid);
            buf.writeBoolean(p.message != null);
            if (p.message != null) buf.writeUtf(p.message);
        }
        private static HandshakeResponseS2C read(RegistryFriendlyByteBuf buf) {
            String ver = buf.readUtf(); int proto = buf.readInt();
            boolean valid = buf.readBoolean();
            String msg = buf.readBoolean() ? buf.readUtf() : null;
            return new HandshakeResponseS2C(ver, proto, valid, msg);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record GenerationStatusS2C(String callId, String status) implements CustomPacketPayload {
        public static final Type<GenerationStatusS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "generation_status"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GenerationStatusS2C> CODEC =
                StreamCodec.of(GenerationStatusS2C::write, GenerationStatusS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, GenerationStatusS2C p) {
            buf.writeUtf(p.callId); buf.writeUtf(p.status);
        }
        private static GenerationStatusS2C read(RegistryFriendlyByteBuf buf) {
            return new GenerationStatusS2C(buf.readUtf(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record GenerationCompleteS2C(String songId, String prompt, int duration,
                                         int seed, String audioUrl, long generatedAt) implements CustomPacketPayload {
        public static final Type<GenerationCompleteS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "generation_complete"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GenerationCompleteS2C> CODEC =
                StreamCodec.of(GenerationCompleteS2C::write, GenerationCompleteS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, GenerationCompleteS2C p) {
            buf.writeUtf(p.songId); buf.writeUtf(p.prompt); buf.writeInt(p.duration);
            buf.writeInt(p.seed); buf.writeUtf(p.audioUrl); buf.writeLong(p.generatedAt);
        }
        private static GenerationCompleteS2C read(RegistryFriendlyByteBuf buf) {
            return new GenerationCompleteS2C(buf.readUtf(), buf.readUtf(), buf.readInt(),
                    buf.readInt(), buf.readUtf(), buf.readLong());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record BroadcastStartS2C(String songId, String audioUrl, String djName,
                                     int durationSeconds, String prompt) implements CustomPacketPayload {
        public static final Type<BroadcastStartS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "broadcast_start"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BroadcastStartS2C> CODEC =
                StreamCodec.of(BroadcastStartS2C::write, BroadcastStartS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, BroadcastStartS2C p) {
            buf.writeUtf(p.songId); buf.writeUtf(p.audioUrl); buf.writeUtf(p.djName);
            buf.writeInt(p.durationSeconds); buf.writeUtf(p.prompt);
        }
        private static BroadcastStartS2C read(RegistryFriendlyByteBuf buf) {
            return new BroadcastStartS2C(buf.readUtf(), buf.readUtf(), buf.readUtf(),
                    buf.readInt(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record BroadcastStopS2C(String reason) implements CustomPacketPayload {
        public static final Type<BroadcastStopS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "broadcast_stop"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BroadcastStopS2C> CODEC =
                StreamCodec.of(BroadcastStopS2C::write, BroadcastStopS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, BroadcastStopS2C p) {
            buf.writeUtf(p.reason);
        }
        private static BroadcastStopS2C read(RegistryFriendlyByteBuf buf) {
            return new BroadcastStopS2C(buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record BroadcastQueueUpdateS2C(int queueSize, int playerPosition,
                                           String currentDj) implements CustomPacketPayload {
        public static final Type<BroadcastQueueUpdateS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "broadcast_queue_update"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BroadcastQueueUpdateS2C> CODEC =
                StreamCodec.of(BroadcastQueueUpdateS2C::write, BroadcastQueueUpdateS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, BroadcastQueueUpdateS2C p) {
            buf.writeInt(p.queueSize); buf.writeInt(p.playerPosition); buf.writeUtf(p.currentDj);
        }
        private static BroadcastQueueUpdateS2C read(RegistryFriendlyByteBuf buf) {
            return new BroadcastQueueUpdateS2C(buf.readInt(), buf.readInt(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record VanillaMusicControlS2C(boolean suppress) implements CustomPacketPayload {
        public static final Type<VanillaMusicControlS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "vanilla_music_control"));
        public static final StreamCodec<RegistryFriendlyByteBuf, VanillaMusicControlS2C> CODEC =
                StreamCodec.of(VanillaMusicControlS2C::write, VanillaMusicControlS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, VanillaMusicControlS2C p) {
            buf.writeBoolean(p.suppress);
        }
        private static VanillaMusicControlS2C read(RegistryFriendlyByteBuf buf) {
            return new VanillaMusicControlS2C(buf.readBoolean());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record NowPlayingS2C(String songId, String prompt, String djName,
                                 int elapsedSeconds, int totalSeconds) implements CustomPacketPayload {
        public static final Type<NowPlayingS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "now_playing"));
        public static final StreamCodec<RegistryFriendlyByteBuf, NowPlayingS2C> CODEC =
                StreamCodec.of(NowPlayingS2C::write, NowPlayingS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, NowPlayingS2C p) {
            buf.writeUtf(p.songId); buf.writeUtf(p.prompt); buf.writeUtf(p.djName);
            buf.writeInt(p.elapsedSeconds); buf.writeInt(p.totalSeconds);
        }
        private static NowPlayingS2C read(RegistryFriendlyByteBuf buf) {
            return new NowPlayingS2C(buf.readUtf(), buf.readUtf(), buf.readUtf(),
                    buf.readInt(), buf.readInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── Registration ─────────────────────────────────────────────────────

    public static void register() {
        // C2S
        PayloadTypeRegistry.playC2S().register(HandshakeC2S.TYPE, HandshakeC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(GenerateRequestC2S.TYPE, GenerateRequestC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(BroadcastRequestC2S.TYPE, BroadcastRequestC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(PlaybackActionC2S.TYPE, PlaybackActionC2S.CODEC);

        // S2C
        PayloadTypeRegistry.playS2C().register(HandshakeResponseS2C.TYPE, HandshakeResponseS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(GenerationStatusS2C.TYPE, GenerationStatusS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(GenerationCompleteS2C.TYPE, GenerationCompleteS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(BroadcastStartS2C.TYPE, BroadcastStartS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(BroadcastStopS2C.TYPE, BroadcastStopS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(BroadcastQueueUpdateS2C.TYPE, BroadcastQueueUpdateS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(VanillaMusicControlS2C.TYPE, VanillaMusicControlS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(NowPlayingS2C.TYPE, NowPlayingS2C.CODEC);
    }
}
