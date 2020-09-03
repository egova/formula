package tk.fishfish.formula.plugin;

import tk.fishfish.formula.annotation.FormulaMapping;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * 自定义公式
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class CustomPlugin implements Plugin {

    /**
     * 实现自己的公式，echo
     *
     * @param name 参数
     * @return 结果
     */
    @FormulaMapping("ECHO")
    public String echo(String name) {
        return "echo: " + name;
    }

    /**
     * 返回所有数字相加的和
     *
     * @param array 数值集合
     * @return 相加的和
     */
    @FormulaMapping("SUM")
    public BigDecimal sum(Object... array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return Arrays.stream(array)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(BigDecimal::new)
                .reduce(BigDecimal::add)
                .orElse(null);
    }

}
