package tk.fishfish.formula.dag;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;
import tk.fishfish.formula.Formula;
import tk.fishfish.formula.exception.DagException;
import tk.fishfish.formula.util.StringUtils;

import java.util.Objects;

/**
 * 公式任务
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class FormulaTask implements Runnable {

    private static final int MAX_SIZE = 100;

    private final Formula formula;

    private final Binding binding;

    private final FormulaData formulaData;

    public FormulaTask(Formula formula, Binding binding, FormulaData formulaData) {
        this.formula = formula;
        this.binding = binding;
        this.formulaData = formulaData;
    }

    @Override
    public void run() {
        Object result = formulaData.getValue();
        // 初始值先放入上下文，防止自引用问题
        if (result != null) {
            // 明细表索引
            if (formulaData.getIndex() > -1) {
                binding.setVariable(formulaData.getName() + "[" + formulaData.getIndex() + "]", result);
            } else {
                binding.setVariable(formulaData.getName(), result);
            }
        }
        if (StringUtils.isNotEmpty(formulaData.getFormula())) {
            result = formula.run(formulaData.getFormula(), binding);
        }
        // 计算出的结果放入上下文
        if (formulaData.getIndex() > -1) {
            Object[] values;
            try {
                values = (Object[]) binding.getVariable(formulaData.getName());
            } catch (MissingPropertyException e) {
                values = new Object[MAX_SIZE];
                binding.setVariable(formulaData.getName(), values);
            }
            if (formulaData.getIndex() >= values.length) {
                throw new DagException("dag array max size: " + values.length);
            }
            values[formulaData.getIndex()] = result;
            // 明细表索引
            binding.setVariable(formulaData.getName() + "[" + formulaData.getIndex() + "]", result);
        } else {
            binding.setVariable(formulaData.getName(), result);
        }
        // 设置结果
        formulaData.setValue(result);
    }

    public String getName() {
        return formulaData.getName();
    }

    public int getIndex() {
        return formulaData.getIndex();
    }

    public String getFormula() {
        return formulaData.getFormula();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormulaTask that = (FormulaTask) o;
        return formulaData.equals(that.formulaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formulaData);
    }
}
