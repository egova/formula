package tk.fishfish.formula.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.fishfish.formula.annotation.FormulaMapping;
import tk.fishfish.formula.exception.FormulaException;
import tk.fishfish.formula.exception.RegistryFormulaException;
import tk.fishfish.formula.plugin.Plugin;
import tk.fishfish.formula.reflect.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 公式脚本
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class FormulaScript extends BaseScript {

    private static final Logger LOG = LoggerFactory.getLogger(FormulaScript.class);

    private static final Map<String, Invocation> PLUGIN_METHOD_MAPPING = new HashMap<>();

    static {
        loadSpiMappings();
    }

    @Override
    public Object invokeMethod(String name, Object args) {
        Invocation invocation = PLUGIN_METHOD_MAPPING.get(name);
        if (invocation == null) {
            throw new FormulaException("can not found method for formula: " + name);
        }
        try {
            return invocation.invoke((Object[]) args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new FormulaException("invoke method failed", e);
        }
    }

    public static void installPlugin(Class<? extends Plugin> pluginClazz) {
        final Plugin instance;
        try {
            instance = pluginClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FormulaException("plugin can not instantiation", e);
        }
        installPlugin(instance);
    }

    public static void installPlugin(Plugin plugin) {
        Class<? extends Plugin> pluginClazz = plugin.getClass();
        LOG.info("install formula plugin: {}", pluginClazz.getName());
        Arrays.stream(pluginClazz.getMethods())
                .filter(method -> method.isAnnotationPresent(FormulaMapping.class))
                .forEach(method -> {
                    FormulaMapping mapping = method.getDeclaredAnnotation(FormulaMapping.class);
                    String alias = mapping.value();
                    if (PLUGIN_METHOD_MAPPING.get(alias) != null) {
                        throw new RegistryFormulaException("formula name [" + alias + "] already exist.");
                    }
                    PLUGIN_METHOD_MAPPING.put(alias, new Invocation(method, plugin));
                    LOG.debug("mapping {} -> {}.{}", alias, pluginClazz.getName(), method.getName());
                });
    }

    private static void loadSpiMappings() {
        LOG.info("loading service provider interface formula.");
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        loader.iterator().forEachRemaining(plugin -> installPlugin(plugin.getClass()));
    }

}
