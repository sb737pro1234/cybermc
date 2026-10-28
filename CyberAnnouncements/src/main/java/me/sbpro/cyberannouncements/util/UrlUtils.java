package me.sbpro.cyberannouncements.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public final class UrlUtils {

    private UrlUtils() {
    }

    /**
     * Normalizes user input into a full URL, adding "https://" if no scheme
     * is present (e.g. "www.twitch.tv/foo" or "twitch.tv/foo").
     * Returns null if the result is not a valid http/https URL.
     */
    public static String normalizeUrl(String input) {
        String candidate = input;

        if (!candidate.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
            candidate = "https://" + candidate;
        }

        try {
            URL parsed = URI.create(candidate).toURL();
            String protocol = parsed.getProtocol();
            String host = parsed.getHost();

            if (!("http".equals(protocol) || "https".equals(protocol))) {
                return null;
            }

            if (host == null || host.isEmpty() || !host.contains(".")) {
                return null;
            }

            return candidate;
        } catch (MalformedURLException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Strips a leading "https://" or "http://" for display purposes only.
     * The full URL (with scheme) should still be used for the actual click event.
     */
    public static String stripScheme(String url) {
        if (url.startsWith("https://")) {
            return url.substring("https://".length());
        }
        if (url.startsWith("http://")) {
            return url.substring("http://".length());
        }
        return url;
    }
}
