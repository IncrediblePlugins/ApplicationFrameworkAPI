package com.github.angeschossen.applicationframework.api.handler;

/**
 * Central access point for the ApplicationFramework API.
 * <p>
 * {@code APIHandler} is a singleton that must be initialized once by the
 * framework implementation before any API calls are made. It provides access
 * to framework factories such as {@link ULIDFactory}.
 * </p>
 */
public class APIHandler {
    private final ULIDFactory ULIDFactory;
    private static APIHandler instance;

    /**
     * Returns the singleton {@code APIHandler} instance.
     *
     * @return the active {@code APIHandler}
     * @throws IllegalStateException if the handler has not been initialized yet
     */
    public static APIHandler getInstance() {
        return instance;
    }

    /**
     * Returns the {@link ULIDFactory} used to generate and parse {@code ULID} values.
     *
     * @return the {@link ULIDFactory}
     */
    public ULIDFactory getFactory() {
        return ULIDFactory;
    }

    /**
     * Initializes the singleton {@code APIHandler} with the given {@link ULIDFactory}.
     * <p>
     * This constructor must be called exactly once by the framework implementation
     * at startup. Subsequent calls will throw an {@link IllegalStateException}.
     * </p>
     *
     * @param ULIDFactory the factory to use for ULID generation and parsing
     * @throws IllegalStateException if an {@code APIHandler} instance already exists
     */
    public APIHandler(ULIDFactory ULIDFactory) {
        if (instance != null) {
            throw new IllegalStateException("Already initialized");
        }

        APIHandler.instance = this;
        this.ULIDFactory = ULIDFactory;
    }
}
