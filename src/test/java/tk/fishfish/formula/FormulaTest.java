package tk.fishfish.formula;

import groovy.lang.Binding;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.fishfish.formula.plugin.CustomPlugin;
import tk.fishfish.formula.script.FormulaScript;

/**
 * 公式测试
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class FormulaTest {

    private static final Logger LOG = LoggerFactory.getLogger(FormulaTest.class);

    private Formula formula;

    @BeforeClass
    public static void init() {
        // 安装自己的公式插件
         FormulaScript.installPlugin(CustomPlugin.class);
    }

    @Before
    public void setup() {
        formula = new Formula();
    }

    @Test
    public void echo() {
        Object result = formula.run("ECHO(大侠王波波)");
        LOG.info("result: {}", result);
    }

    @Test
    public void uuid() {
        Object result = formula.run("UUID()");
        LOG.info("result: {}", result);
    }

    @Test
    public void lower() {
        Object result = formula.run("LOWER(ABC)");
        Assert.assertEquals("abc", result);
    }

    @Test
    public void variable() {
        Binding binding = new Binding();
        binding.setVariable("xxx", "ABC");
        Object result = formula.run("LOWER(xxx)", binding);
        Assert.assertEquals("abc", result);
    }

}
