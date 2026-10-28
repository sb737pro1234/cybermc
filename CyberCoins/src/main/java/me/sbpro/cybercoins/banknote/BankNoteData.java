package me.sbpro.cybercoins.banknote;

import java.util.UUID;

public record BankNoteData(
        UUID noteId,
        long amount,
        UUID withdrawnByUuid,
        String withdrawnByName
) {
}