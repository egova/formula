package tk.fishfish.formula;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import tk.fishfish.formula.script.VariableFormulaScript;

/**
 * 公式
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public final class Formula {

    private final GroovyShell shell;

    public Formula() {
        CompilerConfiguration cfg = new CompilerConfiguration();
        cfg.setScriptBaseClass(VariableFormulaScript.class.getName());
        shell = new GroovyShell(new Binding(), cfg);
    }

    /**
     * 运行脚本
     *
     * @param scriptText 公式脚本
     * @return 结果
     */
    public Object run(String scriptText) {
        Script script = shell.parse(scriptText);
        return script.run();
    }

    /**
     * 运行脚本
     *
     * @param scriptText 公式脚本
     * @param binding    绑定上下文
     * @return 结果
     */
    public Object run(String scriptText, Binding binding) {
        Script script = shell.parse(scriptText);
        script.setBinding(binding);
        return script.run();
    }

}
