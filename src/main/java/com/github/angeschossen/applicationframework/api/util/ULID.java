package com.github.angeschossen.applicationframework.api.util;

import com.github.angeschossen.applicationframework.api.handler.APIHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Universally Unique Lexicographically Sortable Identifier (ULID).
 * <p>
 * ULIDs are 128-bit identifiers that are sortable by creation time and encoded
 * as 26-character Crockford Base32 strings, making them both human-readable
 * and URL-safe.
 * </p>
 */
public interface ULID {

    /**
     * Generates a new random ULID using the current timestamp.
     *
     * @return a new randomly generated {@link ULID}
     */
    @NotNull
    static ULID randomULID() {
        return APIHandler.getInstance().getFactory().randomULID();
    }

    /**
     * Parses a ULID from its string representation.
     *
     * @param s the 26-character Crockford Base32 encoded ULID string
     * @return the {@link ULID} corresponding to the given string
     * @throws IllegalArgumentException if the string is not a valid ULID
     */
    @NotNull
    static ULID fromString(@NotNull String s) {
        return APIHandler.getInstance().getFactory().fromString(s);
    }
}
