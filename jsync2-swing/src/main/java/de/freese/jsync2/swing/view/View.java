// Created: 15.04.23
package de.freese.jsync2.swing.view;

import java.awt.Component;

import de.freese.jsync2.swing.Messages;
import de.freese.jsync2.swing.controller.Controller;

/**
 * @author Thomas Freese
 */
public interface View {
    Component init(Controller controller, Messages resourceBundle);
}
