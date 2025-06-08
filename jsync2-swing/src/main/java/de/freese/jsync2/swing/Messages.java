// Created: 12.07.2020
package de.freese.jsync2.swing;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
public class Messages {
    public static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

    private Map<String, String> messageMap = Collections.emptyMap();

    public Messages(final Locale locale) {
        super();

        setLocale(locale);
    }

    public String getString(final String key) {
        String value = messageMap.get(key);

        if (value == null) {
            LOGGER.warn("no message for key: {}", key);

            value = "_" + key + "_";
        }

        return value;
    }

    private void setLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale required");

        final Map<String, String> map = new HashMap<>();

        final ResourceBundle bundle = ResourceBundle.getBundle("bundles/jsync");

        final Enumeration<String> keys = bundle.getKeys();

        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            final String value = bundle.getString(key);

            map.put(key, value);
        }

        messageMap = map;
    }
}
