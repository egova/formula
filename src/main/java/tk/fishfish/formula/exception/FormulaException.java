package tk.fishfish.formula.exception;

/**
 * 公式异常
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class FormulaException extends RuntimeException {

    public FormulaException(String message) {
        super(message);
    }

    public FormulaException(String message, Throwable cause) {
        super(message, cause);
    }

}
