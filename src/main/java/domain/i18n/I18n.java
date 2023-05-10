package domain.i18n;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Splendor's I18n utility class.
 * <p>
 * This class provides translation capabilities to Splendor, from loading translation files, to translating the strings themselves.
 */
public final class I18n {
    private static final Map<String, String> TRANSLATION_KEYS = new HashMap<>();

    /**
     * The locale currently loaded.
     * <p>
     * Once Splendor is fully loaded, this field is guaranteed to contain the currently loaded Locale. Until that point, no guarantees on state is made.
     */
    public static String current_locale = "";

    /**
     * Instantiates a new I18n instance.
     * <p>
     * I18n is a utility class, and should <b>never</b> be instantiated.
     * <p>
     * Instantiating I18n will do nothing.
     */
    private I18n() {}

    /**
     * Loads the keys for a given language, in the format 'lowercase_language (2 letters), underscore (_), UPPERCASE_COUNTRY (2 letters)', like 'en_US' or 'nl_BE'.
     * <p>
     * When the given language is invalid, or could not be found, the currently loaded language will stay loaded.
     * @param language The language code to load the keys for.
     */
    public static void loadTranslationFile(String language) {
        if (current_locale.equals(language)) return;
        try {
            List<String> content = Files.readAllLines(Path.of(Objects.requireNonNull(I18n.class.getClassLoader().getResource("lang/" + language + ".lang")).toURI()));

            current_locale = language;
            TRANSLATION_KEYS.clear();

            for (String line : content) {
                String[] split = line.split("=");

                if (split.length != 2) continue;

                TRANSLATION_KEYS.put(split[0].trim(), split[1].trim().replace("\"", ""));
            }
        } catch (URISyntaxException | IOException | NullPointerException exception) { // Silently fail
            exception.printStackTrace();
        }
    }

    /**
     * Translates a given key, along with possible arguments.
     * @param key The key to translate
     * @param args Optional parameters to be injected into the translated string, if applicable
     * @return The translated String, with eventual parameters, or the key itself, if no translation could be found.
     */
    public static String translate(String key, String... args) {
        String translated = TRANSLATION_KEYS.get(key);

        if (translated == null) return key; // Not Found
        if (args.length != 0) for (byte b = 0; b < args.length; b++) translated = translated.replace("${" + b + "}", args[b]);
        return translated.replace("\\n", "\n");
    }
}