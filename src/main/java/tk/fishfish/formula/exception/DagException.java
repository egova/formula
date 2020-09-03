package tk.fishfish.formula.exception;

/**
 * DAG异常
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class DagException extends FormulaException {

    public DagException(String message) {
        super(message);
    }

    public DagException(String message, Throwable cause) {
        super(message, cause);
    }

}
