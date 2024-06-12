package fi.hsl.jore.importer.feature.batch.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

public final class ResourceUtil {

    private ResourceUtil() {}

    public static String fromResource(final Resource resource) {
        try (final Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
