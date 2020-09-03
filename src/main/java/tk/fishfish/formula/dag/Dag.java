package tk.fishfish.formula.dag;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import com.google.common.graph.Graphs;
import tk.fishfish.formula.exception.DagException;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;
import java.util.Set;

/**
 * directed-acyclic-graph (DAG)
 *
 * @author xuanbo
 * @version 1.0.0
 */
@NotThreadSafe
public final class Dag {

    /**
     * 所有任务
     */
    private final Set<FormulaTask> tasks;

    /**
     * 任务依赖的（多个）任务
     */
    private final ListMultimap<FormulaTask, FormulaTask> dependencies;

    private Dag() {
        tasks = Sets.newHashSet();
        dependencies = ArrayListMultimap.create();
    }

    /**
     * 基于给定的图，创建一个DAG
     *
     * @param graph 期待有向无环图
     * @return Dag
     */
    @SuppressWarnings("UnstableApiUsage")
    public static Dag of(Graph<FormulaTask> graph) {
        if (!graph.isDirected()) {
            throw new DagException("Graph must be directed.");
        }
        if (Graphs.hasCycle(graph)) {
            throw new DagException("Graph has cycle.");
        }
        Dag dag = new Dag();
        Set<FormulaTask> nodes = graph.nodes();
        for (FormulaTask task : nodes) {
            // 前驱任务
            Set<FormulaTask> predecessors = graph.predecessors(task);
            // 任务
            dag.tasks.add(task);
            // 依赖
            dag.dependencies.putAll(task, predecessors);
        }
        return dag;
    }

    /**
     * 返回下一个任务，会删除该任务
     *
     * @return 下一个任务
     */
    public FormulaTask nextTask() {
        FormulaTask task = peekNextTask();
        if (task == null) {
            return null;
        }
        tasks.remove(task);
        return task;
    }

    /**
     * 返回下一个要执行的任务，不会删除该任务。如果某个任务没有依赖任务任务，则直接返回
     */
    private FormulaTask peekNextTask() {
        for (FormulaTask task : tasks) {
            if (dependencies.containsKey(task)) {
                List<FormulaTask> v = dependencies.get(task);
                if (v.isEmpty()) {
                    return task;
                }
            } else {
                return task;
            }
        }
        return null;
    }

    /**
     * 是否存在任务，如果hasNextRunnableTask返回true，还存在任务，则说明图存在环
     */
    public boolean hasTasks() {
        return tasks.size() > 0;
    }

    /**
     * 任务执行成功通知，依赖会删除该任务的
     *
     * @param task 任务
     */
    public void notifyDone(FormulaTask task) {
        // Remove task from the list of remaining dependencies for any other tasks.
        dependencies.entries().removeIf(e -> e.getValue().equals(task));
    }

}
