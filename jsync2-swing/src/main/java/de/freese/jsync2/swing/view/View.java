package de.freese.jsync2.swing.view;

import java.awt.Component;

import de.freese.jsync2.swing.Messages;
import de.freese.jsync2.swing.controller.Controller;

/**
 * @author Thomas Freese
 * @since 15.04.2023
 */
@FunctionalInterface
public interface View {
    Component init(Controller controller, Messages resourceBundle);
}
