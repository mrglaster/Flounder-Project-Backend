package ru.flounder.study.provider.util;
import java.text.Normalizer;
import ru.flounder.study.provider.domain.ModuleStatus;

public class Util {
    public static ModuleStatus intToModuleStatus(int value) {
        if (value > ModuleStatus.values().length || value < 0) {
            throw new IllegalArgumentException("Invalid day value: " + value);
        }
        return ModuleStatus.values()[value];
    }


    public static String slugify(String value, boolean allowUnicode) {
        if (value == null) {
            return "";
        }

        if (allowUnicode) {
            value = Normalizer.normalize(value, Normalizer.Form.NFKC);
        } else {
            value = Normalizer.normalize(value, Normalizer.Form.NFKD);
            value = value.replaceAll("[^\\p{ASCII}]", "");
        }

        value = value.toLowerCase().replaceAll("[^\\w\\s-]", "");
        return value.replaceAll("[-\\s]+", "-").replaceAll("(^[-_]+|[-_]+$)", "");
    }


}
