package me.sbpro.cyberkits.note;

import java.util.UUID;

/** What a Kit Note item carries in its persistent data. */
public record KitNoteData(
        UUID noteId,
        String kitId,
        UUID claimedByUuid,
        String claimedByName
) {
}
