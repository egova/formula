package tk.fishfish.formula.util;

/**
 * string工具类
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utils");
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

}
