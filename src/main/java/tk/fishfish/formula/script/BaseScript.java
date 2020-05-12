package tk.fishfish.formula.script;

import groovy.lang.MissingPropertyException;
import groovy.lang.Script;

/**
 * 基础脚本
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public abstract class BaseScript extends Script {

    @Override
    public Object run() {
        return null;
    }

    @Override
    public Object getProperty(String propertyName) {
        try {
            return super.getProperty(propertyName);
        } catch (MissingPropertyException ignored) {
            // 注意，这里为了实现字符串无需引号功能
            return propertyName;
        }
    }

}
