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

    public Invocation(Method method, Object target) {
        this.method = method;
        this.target = target;
        this.parameterCount = method.getParameterCount();
    }

    public Object invoke(Object[] args) throws InvocationTargetException, IllegalAccessException {
        // 如果方法没有参数，则忽略传递的参数
        if (this.parameterCount == 0) {
            return method.invoke(target);
        }
        // 如果方法只有一个参数，则将参数数组作为一个值传递
        if (this.parameterCount == 1) {
            return method.invoke(target, new Object[]{args});
        }
        // 如果方法有多个参数，则将参数数组传递，按位匹配
        return method.invoke(target, args);
    }

}
