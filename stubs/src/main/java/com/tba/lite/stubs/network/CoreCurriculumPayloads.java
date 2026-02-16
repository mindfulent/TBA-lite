package com.tba.lite.stubs.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stub payload registrations for CoreCurriculum (namespace: corecurriculum).
 * 6 payloads: 1 C2S + 5 S2C.
 * CoreCurriculum uses Yarn mappings at source level, but at runtime the wire format
 * is identical. Mojang mappings equivalents: Identifier = ResourceLocation,
 * writeString = writeUtf, writeEnumConstant = writeEnum, readEnumConstant = readEnum.
 */
public final class CoreCurriculumPayloads {

    private static final String NS = "corecurriculum";

    private CoreCurriculumPayloads() {}

    // ── Enums ────────────────────────────────────────────────────────────

    public enum GuiType {
        TITLE_CATALOG,
        SUBMISSION,
        FEEDBACK,
        SUBMISSION_HISTORY,
        RECOGNITIONS,
        WELCOME
    }

    public enum NotificationType {
        TITLE_GRANTED,
        SUBMISSION_RECEIVED,
        SUBMISSION_REVIEWED,
        ERROR
    }

    // ── C2S ──────────────────────────────────────────────────────────────

    public record SubmitBuildPayload(String buildName, String description,
                                     List<byte[]> screenshotData,
                                     int x, int y, int z,
                                     String dimension, String submissionType) implements CustomPacketPayload {
        public static final Type<SubmitBuildPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "submit_build"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SubmitBuildPayload> CODEC =
                StreamCodec.of(SubmitBuildPayload::write, SubmitBuildPayload::read);
        private static void write(RegistryFriendlyByteBuf buf, SubmitBuildPayload p) {
            buf.writeUtf(p.buildName);
            buf.writeBoolean(p.description != null);
            if (p.description != null) buf.writeUtf(p.description);
            buf.writeVarInt(p.screenshotData.size());
            for (byte[] data : p.screenshotData) buf.writeByteArray(data);
            buf.writeInt(p.x); buf.writeInt(p.y); buf.writeInt(p.z);
            buf.writeUtf(p.dimension); buf.writeUtf(p.submissionType);
        }
        private static SubmitBuildPayload read(RegistryFriendlyByteBuf buf) {
            String name = buf.readUtf();
            String desc = buf.readBoolean() ? buf.readUtf() : null;
            int count = buf.readVarInt();
            List<byte[]> screenshots = new ArrayList<>(count);
            for (int i = 0; i < count; i++) screenshots.add(buf.readByteArray(500_000));
            int x = buf.readInt(); int y = buf.readInt(); int z = buf.readInt();
            String dim = buf.readUtf(); String type = buf.readUtf();
            return new SubmitBuildPayload(name, desc, screenshots, x, y, z, dim, type);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── S2C ──────────────────────────────────────────────────────────────

    public record TitleSyncPayload(List<TitleData> titleCatalog,
                                    Set<String> earnedSlugs,
                                    String activeTitleSlug) implements CustomPacketPayload {
        public record TitleData(String slug, String name, String description,
                                String category, String tier, String displayColor) {}

        public static final Type<TitleSyncPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "title_sync"));
        public static final StreamCodec<RegistryFriendlyByteBuf, TitleSyncPayload> CODEC =
                StreamCodec.of(TitleSyncPayload::write, TitleSyncPayload::read);
        private static void write(RegistryFriendlyByteBuf buf, TitleSyncPayload p) {
            buf.writeInt(p.titleCatalog.size());
            for (TitleData t : p.titleCatalog) {
                buf.writeUtf(t.slug); buf.writeUtf(t.name); buf.writeUtf(t.description);
                buf.writeUtf(t.category); buf.writeUtf(t.tier); buf.writeUtf(t.displayColor);
            }
            buf.writeInt(p.earnedSlugs.size());
            for (String s : p.earnedSlugs) buf.writeUtf(s);
            buf.writeBoolean(p.activeTitleSlug != null);
            if (p.activeTitleSlug != null) buf.writeUtf(p.activeTitleSlug);
        }
        private static TitleSyncPayload read(RegistryFriendlyByteBuf buf) {
            int catalogSize = buf.readInt();
            List<TitleData> catalog = new ArrayList<>(catalogSize);
            for (int i = 0; i < catalogSize; i++) {
                catalog.add(new TitleData(buf.readUtf(), buf.readUtf(), buf.readUtf(),
                        buf.readUtf(), buf.readUtf(), buf.readUtf()));
            }
            int earnedSize = buf.readInt();
            Set<String> earned = new HashSet<>(earnedSize);
            for (int i = 0; i < earnedSize; i++) earned.add(buf.readUtf());
            String active = buf.readBoolean() ? buf.readUtf() : null;
            return new TitleSyncPayload(catalog, earned, active);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record OpenGuiPayload(GuiType guiType, String data) implements CustomPacketPayload {
        public static final Type<OpenGuiPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "open_gui"));
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenGuiPayload> CODEC =
                StreamCodec.of(OpenGuiPayload::write, OpenGuiPayload::read);
        private static void write(RegistryFriendlyByteBuf buf, OpenGuiPayload p) {
            buf.writeEnum(p.guiType);
            buf.writeUtf(p.data != null ? p.data : "");
        }
        private static OpenGuiPayload read(RegistryFriendlyByteBuf buf) {
            GuiType gui = buf.readEnum(GuiType.class);
            String data = buf.readUtf();
            return new OpenGuiPayload(gui, data.isEmpty() ? null : data);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record NotificationPayload(NotificationType notifType, String title,
                                       String message) implements CustomPacketPayload {
        public static final Type<NotificationPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "notification"));
        public static final StreamCodec<RegistryFriendlyByteBuf, NotificationPayload> CODEC =
                StreamCodec.of(NotificationPayload::write, NotificationPayload::read);
        private static void write(RegistryFriendlyByteBuf buf, NotificationPayload p) {
            buf.writeEnum(p.notifType); buf.writeUtf(p.title); buf.writeUtf(p.message);
        }
        private static NotificationPayload read(RegistryFriendlyByteBuf buf) {
            return new NotificationPayload(buf.readEnum(NotificationType.class),
                    buf.readUtf(), buf.readUtf());
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record SubmissionsPayload(List<SubmissionData> submissions) implements CustomPacketPayload {
        public record SubmissionData(String id, String buildName, String status,
                                     String submittedAt, String assessment) {}

        public static final Type<SubmissionsPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "submissions"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SubmissionsPayload> CODEC =
                StreamCodec.of(SubmissionsPayload::write, SubmissionsPayload::read);
        private static void write(RegistryFriendlyByteBuf buf, SubmissionsPayload p) {
            buf.writeInt(p.submissions.size());
            for (SubmissionData s : p.submissions) {
                buf.writeUtf(s.id); buf.writeUtf(s.buildName);
                buf.writeUtf(s.status); buf.writeUtf(s.submittedAt);
                buf.writeBoolean(s.assessment != null);
                if (s.assessment != null) buf.writeUtf(s.assessment);
            }
        }
        private static SubmissionsPayload read(RegistryFriendlyByteBuf buf) {
            int size = buf.readInt();
            List<SubmissionData> subs = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                String id = buf.readUtf(); String name = buf.readUtf();
                String status = buf.readUtf(); String at = buf.readUtf();
                String assessment = buf.readBoolean() ? buf.readUtf() : null;
                subs.add(new SubmissionData(id, name, status, at, assessment));
            }
            return new SubmissionsPayload(subs);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record RecognitionsPayload(List<RecognitionData> recognitions,
                                      List<TitleProgressData> titleProgress) implements CustomPacketPayload {
        public record RecognitionData(String id, String category, String reason,
                                      boolean anonymous, String nominatorName,
                                      String status, String submittedAt) {}
        public record TitleProgressData(String slug, String name, String description,
                                        String displayColor, String tier,
                                        int current, int required, boolean earned) {}

        public static final Type<RecognitionsPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(NS, "recognitions"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RecognitionsPayload> CODEC =
                StreamCodec.of(RecognitionsPayload::write, RecognitionsPayload::read);
        private static void write(RegistryFriendlyByteBuf buf, RecognitionsPayload p) {
            buf.writeInt(p.recognitions.size());
            for (RecognitionData r : p.recognitions) {
                buf.writeUtf(r.id); buf.writeUtf(r.category); buf.writeUtf(r.reason);
                buf.writeBoolean(r.anonymous);
                buf.writeBoolean(r.nominatorName != null);
                if (r.nominatorName != null) buf.writeUtf(r.nominatorName);
                buf.writeUtf(r.status); buf.writeUtf(r.submittedAt);
            }
            buf.writeInt(p.titleProgress.size());
            for (TitleProgressData t : p.titleProgress) {
                buf.writeUtf(t.slug); buf.writeUtf(t.name); buf.writeUtf(t.description);
                buf.writeUtf(t.displayColor); buf.writeUtf(t.tier);
                buf.writeInt(t.current); buf.writeInt(t.required);
                buf.writeBoolean(t.earned);
            }
        }
        private static RecognitionsPayload read(RegistryFriendlyByteBuf buf) {
            int recSize = buf.readInt();
            List<RecognitionData> recs = new ArrayList<>(recSize);
            for (int i = 0; i < recSize; i++) {
                String id = buf.readUtf(); String cat = buf.readUtf(); String reason = buf.readUtf();
                boolean anon = buf.readBoolean();
                String nom = buf.readBoolean() ? buf.readUtf() : null;
                String status = buf.readUtf(); String at = buf.readUtf();
                recs.add(new RecognitionData(id, cat, reason, anon, nom, status, at));
            }
            int progSize = buf.readInt();
            List<TitleProgressData> progs = new ArrayList<>(progSize);
            for (int i = 0; i < progSize; i++) {
                progs.add(new TitleProgressData(buf.readUtf(), buf.readUtf(), buf.readUtf(),
                        buf.readUtf(), buf.readUtf(), buf.readInt(), buf.readInt(), buf.readBoolean()));
            }
            return new RecognitionsPayload(recs, progs);
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    // ── Registration ─────────────────────────────────────────────────────

    public static void register() {
        // C2S
        PayloadTypeRegistry.playC2S().register(SubmitBuildPayload.TYPE, SubmitBuildPayload.CODEC);

        // S2C
        PayloadTypeRegistry.playS2C().register(TitleSyncPayload.TYPE, TitleSyncPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OpenGuiPayload.TYPE, OpenGuiPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(NotificationPayload.TYPE, NotificationPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SubmissionsPayload.TYPE, SubmissionsPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(RecognitionsPayload.TYPE, RecognitionsPayload.CODEC);
    }
}
