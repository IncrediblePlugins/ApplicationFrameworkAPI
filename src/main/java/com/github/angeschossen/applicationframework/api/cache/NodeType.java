package com.github.angeschossen.applicationframework.api.cache;

/**
 * Represents the role of a node in a messaging or event pipeline.
 */
public enum NodeType {
    /** A node that produces and dispatches messages or events. */
    SENDER,
    /** A node that consumes and processes messages or events. */
    RECEIVER
}
