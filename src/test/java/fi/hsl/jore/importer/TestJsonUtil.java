package fi.hsl.jore.importer;

import org.assertj.core.api.Condition;
import org.json.JSONException;
import org.postgresql.util.PGobject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public final class TestJsonUtil {

    public static Condition equalJson(final String expectedJson) {
        return new Condition<PGobject>(value -> {
            try {
                JSONAssert.assertEquals(
                    value.getValue(),
                    expectedJson,
                    JSONCompareMode.STRICT
                );
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        },
            "equal JSON");
    }
}
