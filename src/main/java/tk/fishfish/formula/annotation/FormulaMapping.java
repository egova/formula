package tk.fishfish.formula.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 公式名映射
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormulaMapping {

    /**
     * 公式名，必须唯一
     *
     * @return 公式名
     */
    String value();

}
