package com.tba.lite.stubs.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Stub payload registrations for StreamCraft (namespace: streamcraft-server).
 * 9 payloads: 5 C2S + 4 S2C.
 */
public final class StreamCraftPayloads {

    private static final String NS = "streamcraft-server";

    private StreamCraftPayloads() {}

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

    public record RequestTokenC2S(UUID playerUuid, String trackType) implements CustomPacketPayload {
        public static final Type<RequestTokenC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "request_token"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RequestTokenC2S> CODEC =
                StreamCodec.of(RequestTokenC2S::write, RequestTokenC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, RequestTokenC2S p) {
            buf.writeUUID(p.playerUuid); buf.writeUtf(p.trackType);
        }
        private static RequestTokenC2S read(RegistryFriendlyByteBuf buf) {
            return new RequestTokenC2S(buf.readUUID(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record StreamUpdateC2S(boolean streaming, List<String> trackTypes) implements CustomPacketPayload {
        public static final Type<StreamUpdateC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "stream_update"));
        public static final StreamCodec<RegistryFriendlyByteBuf, StreamUpdateC2S> CODEC =
                StreamCodec.of(StreamUpdateC2S::write, StreamUpdateC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, StreamUpdateC2S p) {
            buf.writeBoolean(p.streaming);
            buf.writeInt(p.trackTypes.size());
            for (String t : p.trackTypes) buf.writeUtf(t);
        }
        private static StreamUpdateC2S read(RegistryFriendlyByteBuf buf) {
            boolean streaming = buf.readBoolean();
            int size = buf.readInt();
            List<String> types = new ArrayList<>(size);
            for (int i = 0; i < size; i++) types.add(buf.readUtf());
            return new StreamUpdateC2S(streaming, types);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record DisplayAppearanceC2S(BlockPos pos, int appearanceOrdinal) implements CustomPacketPayload {
        public static final Type<DisplayAppearanceC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "display_appearance"));
        public static final StreamCodec<RegistryFriendlyByteBuf, DisplayAppearanceC2S> CODEC =
                StreamCodec.of(DisplayAppearanceC2S::write, DisplayAppearanceC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, DisplayAppearanceC2S p) {
            buf.writeBlockPos(p.pos); buf.writeInt(p.appearanceOrdinal);
        }
        private static DisplayAppearanceC2S read(RegistryFriendlyByteBuf buf) {
            return new DisplayAppearanceC2S(buf.readBlockPos(), buf.readInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record DisplayStreamingC2S(BlockPos pos, boolean streaming) implements CustomPacketPayload {
        public static final Type<DisplayStreamingC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "display_streaming"));
        public static final StreamCodec<RegistryFriendlyByteBuf, DisplayStreamingC2S> CODEC =
                StreamCodec.of(DisplayStreamingC2S::write, DisplayStreamingC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, DisplayStreamingC2S p) {
            buf.writeBlockPos(p.pos); buf.writeBoolean(p.streaming);
        }
        private static DisplayStreamingC2S read(RegistryFriendlyByteBuf buf) {
            return new DisplayStreamingC2S(buf.readBlockPos(), buf.readBoolean());
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

    public record TokenResponseS2C(String token, String serverUrl, String roomName,
                                    int expiresIn, String error) implements CustomPacketPayload {
        public static final Type<TokenResponseS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "token_response"));
        public static final StreamCodec<RegistryFriendlyByteBuf, TokenResponseS2C> CODEC =
                StreamCodec.of(TokenResponseS2C::write, TokenResponseS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, TokenResponseS2C p) {
            buf.writeBoolean(p.error != null);
            if (p.error != null) {
                buf.writeUtf(p.error);
            } else {
                buf.writeUtf(p.token); buf.writeUtf(p.serverUrl);
                buf.writeUtf(p.roomName); buf.writeInt(p.expiresIn);
            }
        }
        private static TokenResponseS2C read(RegistryFriendlyByteBuf buf) {
            if (buf.readBoolean()) {
                return new TokenResponseS2C(null, null, null, 0, buf.readUtf());
            }
            return new TokenResponseS2C(buf.readUtf(), buf.readUtf(), buf.readUtf(), buf.readInt(), null);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record VisibilityS2C(UUID streamerUuid, String streamerName,
                                 boolean visible, List<String> trackTypes) implements CustomPacketPayload {
        public static final Type<VisibilityS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "visibility"));
        public static final StreamCodec<RegistryFriendlyByteBuf, VisibilityS2C> CODEC =
                StreamCodec.of(VisibilityS2C::write, VisibilityS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, VisibilityS2C p) {
            buf.writeUUID(p.streamerUuid); buf.writeUtf(p.streamerName);
            buf.writeBoolean(p.visible);
            buf.writeInt(p.trackTypes.size());
            for (String t : p.trackTypes) buf.writeUtf(t);
        }
        private static VisibilityS2C read(RegistryFriendlyByteBuf buf) {
            UUID uuid = buf.readUUID(); String name = buf.readUtf();
            boolean visible = buf.readBoolean();
            int size = buf.readInt();
            List<String> types = new ArrayList<>(size);
            for (int i = 0; i < size; i++) types.add(buf.readUtf());
            return new VisibilityS2C(uuid, name, visible, types);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ViewersS2C(List<String> viewerNames) implements CustomPacketPayload {
        public static final Type<ViewersS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "viewers"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ViewersS2C> CODEC =
                StreamCodec.of(ViewersS2C::write, ViewersS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, ViewersS2C p) {
            buf.writeInt(p.viewerNames.size());
            for (String n : p.viewerNames) buf.writeUtf(n);
        }
        private static ViewersS2C read(RegistryFriendlyByteBuf buf) {
            int size = buf.readInt();
            List<String> names = new ArrayList<>(size);
            for (int i = 0; i < size; i++) names.add(buf.readUtf());
            return new ViewersS2C(names);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── Registration ─────────────────────────────────────────────────────

    public static void register() {
        // C2S
        PayloadTypeRegistry.playC2S().register(HandshakeC2S.TYPE, HandshakeC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(RequestTokenC2S.TYPE, RequestTokenC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(StreamUpdateC2S.TYPE, StreamUpdateC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(DisplayAppearanceC2S.TYPE, DisplayAppearanceC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(DisplayStreamingC2S.TYPE, DisplayStreamingC2S.CODEC);

        // S2C
        PayloadTypeRegistry.playS2C().register(HandshakeResponseS2C.TYPE, HandshakeResponseS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(TokenResponseS2C.TYPE, TokenResponseS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(VisibilityS2C.TYPE, VisibilityS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ViewersS2C.TYPE, ViewersS2C.CODEC);
    }
}
