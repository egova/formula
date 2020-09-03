package tk.fishfish.formula;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import groovy.lang.Binding;
import tk.fishfish.formula.dag.Dag;
import tk.fishfish.formula.dag.FormulaData;
import tk.fishfish.formula.dag.FormulaTask;
import tk.fishfish.formula.exception.DagException;
import tk.fishfish.formula.exception.FormulaException;
import tk.fishfish.formula.exception.FormulaTaskException;
import tk.fishfish.formula.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * DAG公式计算引擎
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public final class DagFormulaEngine {

    private static final Pattern VARIABLE_REGEX = Pattern.compile("#\\{([^{}]+?)}");

    public static final String BINDING_CURRENT_INDEX = "_index";
    public static final String BINDING_TASKS = "_tasks";

    private final Formula formula;

    public DagFormulaEngine() {
        this(new Formula());
    }

    public DagFormulaEngine(Formula formula) {
        this.formula = formula;
    }

    /**
     * 公式计算
     *
     * @param formulaDataList 批量计算数据
     * @throws FormulaTaskException 公式计算异常
     * @throws DagException         DAG构造不合法
     * @throws FormulaException     公式计算异常
     */
    @SuppressWarnings("UnstableApiUsage")
    public void run(List<FormulaData> formulaDataList) {
        // 绑定的数据上下文
        Binding binding = new Binding();
        // 图
        MutableGraph<FormulaTask> graph = GraphBuilder.directed().build();

        List<FormulaTask> tasks = formulaDataList.stream()
                .map(formulaData -> new FormulaTask(formula, binding, formulaData))
                .collect(Collectors.toList());

        // tasks
        binding.setVariable(BINDING_TASKS, tasks);

        // 顶点
        tasks.forEach(graph::addNode);

        Map<String, List<FormulaTask>> taskMap = tasks.stream().collect(Collectors.groupingBy(FormulaTask::getName));
        // 边
        tasks.forEach(toTask ->
                findVariables(toTask.getFormula()).stream()
                        .map(taskMap::get)
                        .filter(Objects::nonNull)
                        .forEach(fromTasks -> fromTasks.forEach(fromTask -> {
                            // 自己引用自己
                            if (fromTask == toTask) {
                                return;
                            }
                            graph.putEdge(fromTask, toTask);
                        }))
        );

        // 图转 DAG
        Dag dag = Dag.of(graph);
        // 计算
        runTasks(dag, binding);
    }

    private void runTasks(Dag taskGraph, Binding binding) {
        while (true) {
            FormulaTask task = taskGraph.nextTask();
            if (task == null) {
                if (taskGraph.hasTasks()) {
                    continue;
                }
                return;
            }
            try {
                binding.setVariable(BINDING_CURRENT_INDEX, task.getIndex());
                task.run();
            } catch (Throwable err) {
                // 计算失败直接返回
                if (err instanceof FormulaException) {
                    throw new FormulaTaskException(task, err.getMessage(), err.getCause());
                }
                throw new FormulaTaskException(task, err);
            } finally {
                taskGraph.notifyDone(task);
                binding.removeVariable(BINDING_CURRENT_INDEX);
            }
        }
    }

    private static List<String> findVariables(String scriptText) {
        if (StringUtils.isEmpty(scriptText)) {
            return Collections.emptyList();
        }
        Matcher matcher = VARIABLE_REGEX.matcher(scriptText);
        List<String> variables = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            variables.add(group);
        }
        return variables;
    }

}
