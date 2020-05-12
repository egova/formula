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

    public Invocation(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }

}
