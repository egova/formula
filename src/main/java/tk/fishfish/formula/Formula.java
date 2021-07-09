package tk.fishfish.formula;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.EncodingGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import tk.fishfish.formula.exception.FormulaException;
import tk.fishfish.formula.script.VariableFormulaScript;

import java.util.concurrent.TimeUnit;

/**
 * 公式
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public final class Formula {

    private final CompilerConfiguration cfg;
    private final Cache<String, Class<Script>> scriptCache;

    public Formula() {
        cfg = new CompilerConfiguration();
        cfg.setScriptBaseClass(VariableFormulaScript.class.getName());
        scriptCache = CacheBuilder.newBuilder()
                .maximumSize(1024)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 运行脚本
     *
     * @param scriptText 公式脚本
     * @return 结果
     */
    public Object run(String scriptText) {
        return run(scriptText, new Binding());
    }

    /**
     * 运行脚本
     *
     * @param scriptText 公式脚本
     * @param binding    绑定上下文
     * @return 结果
     */
    @SuppressWarnings("unchecked")
    public Object run(String scriptText, Binding binding) {
        Class<Script> scriptClass;
        try {
            String key = EncodingGroovyMethods.md5(scriptText);
            scriptClass = scriptCache.get(key, () -> {
                // 创建一个新的GroovyClassLoader，防止共用一个类导致无法卸载class
                GroovyClassLoader classLoader = new GroovyClassLoader(VariableFormulaScript.class.getClassLoader(), cfg);
                return classLoader.parseClass(scriptText);
            });
        } catch (Exception e) {
            throw new FormulaException("加载缓存失败", e);
        }
        Script script = InvokerHelper.createScript(scriptClass, binding);
        return script.run();
    }

    /**
     * 运行java代码块
     *
     * @param javaCode java代码块
     * @param binding  绑定上下文
     * @return 结果
     */
    @SuppressWarnings("unchecked")
    public Object runJava(String javaCode, Binding binding) {
        Class<Script> scriptClass;
        try {
            String key = EncodingGroovyMethods.md5(javaCode);
            scriptClass = scriptCache.get(key, () -> {
                // 创建一个新的GroovyClassLoader，防止共用一个类导致无法卸载class
                GroovyClassLoader classLoader = new GroovyClassLoader(VariableFormulaScript.class.getClassLoader(), cfg);
                return classLoader.parseClass(javaCode);
            });
        } catch (Exception e) {
            throw new FormulaException("加载缓存失败", e);
        }
        Script script = InvokerHelper.createScript(scriptClass, binding);
        return script.evaluate(javaCode);
    }

}
