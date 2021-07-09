package tk.fishfish.formula.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 调用对象
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class Invocation {

    private final Method method;
    private final Object target;
    private final int parameterCount;
    private final Class<?>[] parameterTypes;

    public Invocation(Method method, Object target) {
        this.method = method;
        this.target = target;
        this.parameterCount = method.getParameterCount();
        this.parameterTypes = method.getParameterTypes();
    }

    public Object invoke(Object[] args) throws InvocationTargetException, IllegalAccessException {
        // 如果方法没有参数，则忽略传递的参数
        if (this.parameterCount == 0) {
            return method.invoke(target);
        }
        // 如果方法只有一个参数
        if (this.parameterCount == 1) {
            // 如果是数组（不定数组），则作为一个参数传递
            if (parameterTypes[0].isArray()) {
                return method.invoke(target, new Object[]{args});
            }
            // 不是数组，则取第一个值传递
            return method.invoke(target, args == null || args.length == 0 ? null : args[0]);
        }
        // 如果方法有多个参数，则将参数数组传递，按位匹配
        return method.invoke(target, args);
    }

}
