package tk.fishfish.formula;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.fishfish.formula.dag.FormulaData;
import tk.fishfish.formula.plugin.CustomPlugin;
import tk.fishfish.formula.script.FormulaScript;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class DagFormulaEngineTest {

    private static final Logger LOG = LoggerFactory.getLogger(FormulaTest.class);

    private DagFormulaEngine engine;

    @BeforeClass
    public static void init() {
        // 安装自己的公式插件
        FormulaScript.installPlugin(CustomPlugin.class);
    }

    @Before
    public void setup() {
        engine = new DagFormulaEngine(new Formula());
    }

    @Test
    public void run() {
        // 批量计算的公式、数据
        List<FormulaData> formulaDataList = new ArrayList<FormulaData>() {{
            add(new FormulaData("field1", 0, null, 1));
            add(new FormulaData("field1", 1, null, 3));
            add(new FormulaData("field1", 2, null, 5));
            add(new FormulaData("field2", -1, null, "ABC"));
            add(new FormulaData("field3", -1, "LOWER('#{field2}')", null));
            add(new FormulaData("field4", -1, "SUM('#{field1}')", 0));
        }};
        // 计算
        engine.run(formulaDataList);
        // 结果
        formulaDataList.forEach(formulaData ->
                LOG.info("name: {}, formula: {}, value: {}",
                        formulaData.getName(), formulaData.getFormula(), formulaData.getValue())
        );
    }

}
