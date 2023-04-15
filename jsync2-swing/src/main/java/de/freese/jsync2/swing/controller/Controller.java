// Created: 15.04.23
package de.freese.jsync2.swing.controller;

import java.awt.Component;

import de.freese.jsync2.swing.Messages;

/**
 * @author Thomas Freese
 */
public interface Controller {
    Component init(Messages resourceBundle);

    void shutdown();
}
