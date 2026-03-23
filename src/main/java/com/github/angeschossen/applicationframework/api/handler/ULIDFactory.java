package com.github.angeschossen.applicationframework.api.handler;

import com.github.angeschossen.applicationframework.api.util.ULID;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating {@link ULID} instances.
 * <p>
 * Implementations are responsible for generating and parsing ULIDs.
 * Obtain an instance via {@link APIHandler#getFactory()}.
 * </p>
 */
public interface ULIDFactory {

    /**
     * Generates a new random ULID using the current timestamp.
     *
     * @return a new randomly generated {@link ULID}
     */
    @NotNull
    ULID randomULID();

    /**
     * Parses a ULID from its string representation.
     *
     * @param s the 26-character Crockford Base32 encoded ULID string
     * @return the {@link ULID} corresponding to the given string
     * @throws IllegalArgumentException if the string is not a valid ULID
     */
    @NotNull
    ULID fromString(@NotNull String s);
}
