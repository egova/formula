package tk.fishfish.formula.dag;

import java.io.Serializable;
import java.util.Objects;

/**
 * 公式数据
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public final class FormulaData implements Serializable {

    /**
     * 唯一名称
     */
    private String name;

    /**
     * 索引
     */
    private int index;

    /**
     * 公式
     */
    private String formula;

    /**
     * 数据
     */
    private Object value;

    public FormulaData(String name, int index, String formula, Object value) {
        this.name = name;
        this.index = index;
        this.formula = formula;
        this.value = value;
    }

    public FormulaData(String name, String formula, Object value) {
        this(name, -1, formula, value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormulaData that = (FormulaData) o;
        return index == that.index && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, index);
    }
}
