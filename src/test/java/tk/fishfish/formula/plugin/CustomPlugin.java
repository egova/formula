package tk.fishfish.formula.plugin;

import tk.fishfish.formula.annotation.FormulaMapping;

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

}
