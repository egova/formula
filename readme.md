# formula

> 基于 groovy 实现的公式库

## 语法

```
公式名(参数)
```

比如：

```
ECHO(大侠王波波)
```

支持公式嵌套：

```
公式名1(公式名2(参数), 参数)
```

比如：

```
ECHO(UUID())
```

## 快速开始

1. 创建 Formula 对象 formula
1. 运行 formula.run("script")

下面是例子：

```java
package tk.fishfish.formula;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 公式测试
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class FormulaTest {

    private Formula formula;

    @Before
    public void setup() {
        formula = new Formula();
    }

    @Test
    public void lower() {
        Object result = formula.run("LOWER(ABC)");
        Assert.assertEquals("abc", result);
    }

}

```

## 默认公式

这里只是抛砖引玉，实现了如下文本公式：

- UUID() 返回uuid
- LOWER(xxx) 转小写
- UPPER(xxx) 转大写

这里未实现丰富文本、时间、数学、逻辑等公式，只是提供一个扩展机制，方便大家定制自己的公式库。

## 开发自己的公式

- 继承 Plugin 接口

    ```java
    package tk.fishfish.formula.plugin;

    import tk.fishfish.formula.annotation.FormulaMapping;
    
    /**
     * 自定义公式
     *
     * @author 奔波儿灞
     * @since 1.0
     */
    public class CustomPlugin implements Plugin {
    
        /**
         * 实现自己的公式
         *
         * @param name 参数
         * @return 结果
         */
        @FormulaMapping("ECHO")
        public String echo(String name) {
            return "echo: " + name;
        }
    
    }
    
    ```

- 自定义方法，增加 @FormulaMapping 注解 映射公式名称
- 安装插件类，FormulaScript.installPlugin(CustomPlugin.class)

    ```java
    package tk.fishfish.formula;
    
    import org.junit.Assert;
    import org.junit.Before;
    import org.junit.Test;
    import tk.fishfish.formula.plugin.CustomPlugin;
    import tk.fishfish.formula.script.FormulaScript;
    
    import java.math.BigDecimal;
    
    /**
     * 公式测试
     *
     * @author 奔波儿灞
     * @since 1.0
     */
    public class FormulaTest {
    
        private Formula formula;
    
        @BeforeClass
        public static void init() {
            // 安装自己的公式插件
            FormulaScript.installPlugin(CustomPlugin.class);
        }
    
        @Before
        public void setup() {
            formula = new Formula();
        }
    
        @Test
        public void plugin() {
            Object result = formula.run("ECHO(xxx)");
            System.out.println(result);
        }
    
    }
    
    ```

注意：

- 先安装自己的公式，再创建 Formula 对象
- 公式名全局不可重复

## SPI扩展

除了可以手动 FormulaScript.installPlugin(CustomPlugin.class) 安装自定义公式以外，还能通过 SPI 注册。

在 src/main/resources/META-INF/services 目录下创建名称为 tk.fishfish.formula.plugin.Plugin 的文件，里面是实现类的全类名：

```
# custom plugin
tk.fishfish.formula.plugin.CustomPlugin
```

此时，会自动通过 SPI 机制发现实现类，自动安装，实现解偶。

## 关于

### 变量

大部分情况下，业务定制的公式都会含有变量，比如：

```
公式名(变量)
```

一般地，都会在运行时，替换变量。

```java
package tk.fishfish.formula;

import groovy.lang.Binding;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.fishfish.formula.plugin.CustomPlugin;
import tk.fishfish.formula.script.FormulaScript;

/**
 * 公式测试
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class FormulaTest {

    private Formula formula;

    @Before
    public void setup() {
        formula = new Formula();
    }

    @Test
    public void variable() {
        Binding binding = new Binding();
        binding.setVariable("xxx", "ABC");
        // 传入 Binding 上下文
        Object result = formula.run("LOWER(xxx)", binding);
        Assert.assertEquals("abc", result);
    }

}

```

### 依赖

更有甚者，存在依赖关系，比如：

- 值 A 的公式为：`公式1(xxx)`
- 值 B 的公式为：`公式2(A)`，注意 B 依赖 A 的结果

此时，就存在依赖关系了，即 B 依赖 A

这里未提供代码解决该场景，这里可以给予提示，依赖关系可转化为 
DAG（有向无环图）计算。

该部分代码暂不开源。

### 数据库公式

这个也是业务必备，这里仍不开源。

大家可采用 groovy-sql 或者 JdbcTemplate
实现即可。
