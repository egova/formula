package tk.fishfish.formula.plugin;

import tk.fishfish.formula.annotation.FormulaMapping;

import java.util.UUID;

/**
 * 文本公式
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class TextPlugin implements Plugin {

    /**
     * 返回UUID
     *
     * @return UUID
     */
    @FormulaMapping("UUID")
    public String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 文本转小写
     *
     * @param text 文本
     * @return 小写
     */
    @FormulaMapping("LOWER")
    public String lower(String text) {
        return text.toLowerCase();
    }

    /**
     * 文本转大写
     *
     * @param text 文本
     * @return 大写
     */
    @FormulaMapping("UPPER")
    public String upper(String text) {
        return text.toUpperCase();
    }

}
