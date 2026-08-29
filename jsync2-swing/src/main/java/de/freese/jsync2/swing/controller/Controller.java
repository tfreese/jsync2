package de.freese.jsync2.swing.controller;

import java.awt.Component;

import de.freese.jsync2.swing.Messages;

/**
 * @author Thomas Freese
 * @since 15.04.2023
 */
public interface Controller {
    Component init(Messages resourceBundle);

    void shutdown();
}
