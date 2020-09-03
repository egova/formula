package tk.fishfish.formula.script;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;
import tk.fishfish.formula.DagFormulaEngine;
import tk.fishfish.formula.dag.FormulaTask;
import tk.fishfish.formula.exception.FormulaException;

import java.util.List;

/**
 * 绑定变量公式
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class VariableFormulaScript extends FormulaScript {

    private static final String VARIABLE_START = "#{";
    private static final String VARIABLE_END = "}";

    @Override
    public Object invokeMethod(String name, Object args) {
        // 解析变量
        parseVariables((Object[]) args);
        // 方法调用
        return super.invokeMethod(name, args);
    }

    private void parseVariables(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof String) {
                String argStr = (String) arg;
                if (argStr.startsWith(VARIABLE_START) && argStr.endsWith(VARIABLE_END)) {
                    String variable = argStr.substring(VARIABLE_START.length(), argStr.length() - 1);

                    // 对于明细引用明细，修正为当前行
                    variable = parseVariable(variable, getBinding());

                    // 从绑定上下文中取值
                    Object value = null;
                    try {
                        value = getBinding().getProperty(variable);
                    } catch (MissingPropertyException e) {
                        // ignore
                    }
                    args[i] = value;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String parseVariable(String variable, Binding binding) {
        try {
            int index = (int) binding.getVariable(DagFormulaEngine.BINDING_CURRENT_INDEX);
            if (index > -1) {
                List<FormulaTask> tasks = (List<FormulaTask>) binding.getVariable(DagFormulaEngine.BINDING_TASKS);
                FormulaTask task = tasks.stream()
                        .filter(e -> e.getName().equals(variable))
                        .findFirst()
                        .orElseThrow(() -> new FormulaException("公式引擎错误，找不到_tasks系统变量"));
                if (task.getIndex() > -1) {
                    return variable + "[" + index + "]";
                }
            }
            return variable;
        } catch (MissingPropertyException e) {
            return variable;
        }
    }

}
