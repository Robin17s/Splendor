package domain.i18n;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class I18n {
    private static final Map<String, String> TRANSLATION_KEYS = new HashMap<>();

    private I18n() {}

    public static void loadTranslationFile(String language) {
        try {
            List<String> content = Files.readAllLines(Path.of(Objects.requireNonNull(I18n.class.getClassLoader().getResource("lang/" + language + ".lang")).toURI()));

            for (String line : content) {
                String[] split = line.split("=");

                if (split.length != 2) continue;

                TRANSLATION_KEYS.put(split[0].trim(), split[1].trim().replace("\"", ""));
            }
        } catch (URISyntaxException | IOException | NullPointerException exception) { // Silently fail
            exception.printStackTrace();
        }
    }

    public static String translate(String key, String... args) {
        String translated = TRANSLATION_KEYS.get(key);

        if (translated == null) return key; // Not Found
        if (args.length != 0) for (byte b = 0; b < args.length; b++) translated = translated.replace("${" + b + "}", args[b]);
        return translated.replace("\\n", "\n");
    }
}