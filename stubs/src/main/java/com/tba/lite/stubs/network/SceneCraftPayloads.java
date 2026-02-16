package com.tba.lite.stubs.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Stub payload registrations for SceneCraft (namespace: scenecraft).
 * 7 payloads: 3 C2S + 4 S2C.
 * Note: SceneCraft uses writeVarInt for count/small-integer fields.
 */
public final class SceneCraftPayloads {

    private static final String NS = "scenecraft";

    private SceneCraftPayloads() {}

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

    public record ExportRequestC2S(List<String> highlightIds) implements CustomPacketPayload {
        public static final Type<ExportRequestC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "export_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ExportRequestC2S> CODEC =
                StreamCodec.of(ExportRequestC2S::write, ExportRequestC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, ExportRequestC2S p) {
            buf.writeVarInt(p.highlightIds.size());
            for (String id : p.highlightIds) buf.writeUtf(id);
        }
        private static ExportRequestC2S read(RegistryFriendlyByteBuf buf) {
            int count = buf.readVarInt();
            List<String> ids = new ArrayList<>(count);
            for (int i = 0; i < count; i++) ids.add(buf.readUtf());
            return new ExportRequestC2S(ids);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ExportCompleteC2S(int highlightCount) implements CustomPacketPayload {
        public static final Type<ExportCompleteC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "export_complete"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ExportCompleteC2S> CODEC =
                StreamCodec.of(ExportCompleteC2S::write, ExportCompleteC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, ExportCompleteC2S p) {
            buf.writeVarInt(p.highlightCount);
        }
        private static ExportCompleteC2S read(RegistryFriendlyByteBuf buf) {
            return new ExportCompleteC2S(buf.readVarInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ReportSessionsC2S(int sessionCount) implements CustomPacketPayload {
        public static final Type<ReportSessionsC2S> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "report_sessions"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ReportSessionsC2S> CODEC =
                StreamCodec.of(ReportSessionsC2S::write, ReportSessionsC2S::read);
        private static void write(RegistryFriendlyByteBuf buf, ReportSessionsC2S p) {
            buf.writeVarInt(p.sessionCount);
        }
        private static ReportSessionsC2S read(RegistryFriendlyByteBuf buf) {
            return new ReportSessionsC2S(buf.readVarInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── S2C ──────────────────────────────────────────────────────────────

    public record HandshakeResponseS2C(String serverModVersion, int protocolVersion,
                                        boolean licenseValid, int trialSessionsRemaining,
                                        String message, String serverId) implements CustomPacketPayload {
        public static final Type<HandshakeResponseS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "handshake_response"));
        public static final StreamCodec<RegistryFriendlyByteBuf, HandshakeResponseS2C> CODEC =
                StreamCodec.of(HandshakeResponseS2C::write, HandshakeResponseS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, HandshakeResponseS2C p) {
            buf.writeUtf(p.serverModVersion); buf.writeInt(p.protocolVersion);
            buf.writeBoolean(p.licenseValid); buf.writeInt(p.trialSessionsRemaining);
            buf.writeBoolean(p.message != null);
            if (p.message != null) buf.writeUtf(p.message);
            buf.writeBoolean(p.serverId != null);
            if (p.serverId != null) buf.writeUtf(p.serverId);
        }
        private static HandshakeResponseS2C read(RegistryFriendlyByteBuf buf) {
            String ver = buf.readUtf(); int proto = buf.readInt();
            boolean valid = buf.readBoolean(); int sessions = buf.readInt();
            String msg = buf.readBoolean() ? buf.readUtf() : null;
            String sid = buf.readBoolean() ? buf.readUtf() : null;
            return new HandshakeResponseS2C(ver, proto, valid, sessions, msg, sid);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ExportPermissionS2C(boolean allowed, int sessionsRemaining,
                                       String message, String upgradeUrl) implements CustomPacketPayload {
        public static final Type<ExportPermissionS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "export_permission"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ExportPermissionS2C> CODEC =
                StreamCodec.of(ExportPermissionS2C::write, ExportPermissionS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, ExportPermissionS2C p) {
            buf.writeBoolean(p.allowed); buf.writeInt(p.sessionsRemaining);
            buf.writeBoolean(p.message != null);
            if (p.message != null) buf.writeUtf(p.message);
            buf.writeBoolean(p.upgradeUrl != null);
            if (p.upgradeUrl != null) buf.writeUtf(p.upgradeUrl);
        }
        private static ExportPermissionS2C read(RegistryFriendlyByteBuf buf) {
            boolean allowed = buf.readBoolean(); int remaining = buf.readInt();
            String msg = buf.readBoolean() ? buf.readUtf() : null;
            String url = buf.readBoolean() ? buf.readUtf() : null;
            return new ExportPermissionS2C(allowed, remaining, msg, url);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ReportSessionsAckS2C(int sessionsConsumed) implements CustomPacketPayload {
        public static final Type<ReportSessionsAckS2C> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "report_sessions_ack"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ReportSessionsAckS2C> CODEC =
                StreamCodec.of(ReportSessionsAckS2C::write, ReportSessionsAckS2C::read);
        private static void write(RegistryFriendlyByteBuf buf, ReportSessionsAckS2C p) {
            buf.writeVarInt(p.sessionsConsumed);
        }
        private static ReportSessionsAckS2C read(RegistryFriendlyByteBuf buf) {
            return new ReportSessionsAckS2C(buf.readVarInt());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── Registration ─────────────────────────────────────────────────────

    public static void register() {
        // C2S
        PayloadTypeRegistry.playC2S().register(HandshakeC2S.TYPE, HandshakeC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ExportRequestC2S.TYPE, ExportRequestC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ExportCompleteC2S.TYPE, ExportCompleteC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ReportSessionsC2S.TYPE, ReportSessionsC2S.CODEC);

        // S2C
        PayloadTypeRegistry.playS2C().register(HandshakeResponseS2C.TYPE, HandshakeResponseS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ExportPermissionS2C.TYPE, ExportPermissionS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ReportSessionsAckS2C.TYPE, ReportSessionsAckS2C.CODEC);
    }
}
