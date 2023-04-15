// Created: 15.04.23
package de.freese.jsync2.swing.view;

import java.awt.Component;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import de.freese.jsync2.model.SyncPair;
import de.freese.jsync2.swing.Messages;
import de.freese.jsync2.swing.controller.Controller;

/**
 * @author Thomas Freese
 */
public class DefaultSyncView extends AbstractView implements SyncView {
    @Override
    public void addSyncPair(final SyncPair syncPair) {
        // TODO
    }

    @Override
    public Component init(final Controller controller, final Messages resourceBundle) {
        // TODO
        return null;
    }

    @Override
    public void restoreState() {
        Path path = Paths.get(System.getProperty("user.home"), ".java-apps", "jsync", "jSyncGuiState");
        Properties properties = new Properties();

        if (Files.exists(path)) {
            try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
                properties.load(is);
            }
            catch (Exception ex) {
                getLogger().error(ex.getMessage(), ex);
            }
        }

        //        getTextFieldHostPort(EFileSystem.SENDER).setText(properties.getProperty("sender.textfieldHostPort"));
        //        getTextFieldHostPort(EFileSystem.RECEIVER).setText(properties.getProperty("receiver.textfieldHostPort"));
        //
        //        getTextFieldPath(EFileSystem.SENDER).setText(properties.getProperty("sender.textfieldPath"));
        //        getTextFieldPath(EFileSystem.RECEIVER).setText(properties.getProperty("receiver.textfieldPath"));
        //
        //        getComboBoxProtocol(EFileSystem.SENDER).setSelectedItem(JSyncProtocol.valueOf(properties.getProperty("sender.protocol", "FILE")));
        //        getComboBoxProtocol(EFileSystem.RECEIVER).setSelectedItem(JSyncProtocol.valueOf(properties.getProperty("receiver.protocol", "FILE")));
        //
        //        this.textAreaFilterDirs.setText(properties.getProperty("filter.directories", "target; build; .settings; .idea; .gradle"));
        //        this.textAreaFilterFiles.setText(properties.getProperty("filter.files", ".class; .log"));
    }

    @Override
    public void saveState() {
        Path path = Paths.get(System.getProperty("user.home"), ".java-apps", "jsync", "jSyncGuiState");

        Properties properties = new Properties();

        //        properties.setProperty("filter.directories", this.textAreaFilterDirs.getText());
        //        properties.setProperty("filter.files", this.textAreaFilterFiles.getText());

        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            properties.store(os, null);
        }
        catch (Exception ex) {
            getLogger().error(ex.getMessage(), ex);
        }
    }
}
