package tk.fishfish.formula.exception;

import tk.fishfish.formula.dag.FormulaTask;

/**
 * 公式任务异常
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class FormulaTaskException extends FormulaException {

    private final FormulaTask formulaTask;

    public FormulaTaskException(FormulaTask formulaTask, String message) {
        super(message);
        this.formulaTask = formulaTask;
    }

    public FormulaTaskException(FormulaTask formulaTask, Throwable cause) {
        super(cause.getMessage(), cause);
        this.formulaTask = formulaTask;
    }

    public FormulaTaskException(FormulaTask formulaTask, String message, Throwable cause) {
        super(message, cause);
        this.formulaTask = formulaTask;
    }

    public FormulaTask getFormulaTask() {
        return formulaTask;
    }

}
