package net.wanji.dataset;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class MybatisPlusGenerator {
    //    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String OUTPUT_DIR = "DataOnsite/src/main/java";
    private static final String AUTHOR = "wj";
    private static final String URL = "jdbc:mysql://10.102.1.115:3306/wanji_cdjhs_test_management?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    //    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Wanji123456";
    private static final String[] TABLES = {
            "cdjhs_static_element"
    };
    //    private static final String TEMPLATE_PATH = "/templates/mapper.xml.ftl";
    private static final String MAPPER_PATH = "business/src/main/resources/mapper";
    private static final String PARENT = "net.wanji.onsite";


    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(OUTPUT_DIR) // 指定输出目录
                            .disableOpenDir();//禁止打开输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT); // 设置父包名
                    //.moduleName(null) // 设置父包模块名
//                     .pathInfo(Collections.singletonMap(OutputFile.mapperXml, MAPPER_PATH)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLES) // 设置需要生成的表名
                            //.addTablePrefix(null) // 设置过滤表前缀
                            //Service 策略配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService")//格式化 service 接口文件名称
                            .formatServiceImplFileName("%sServiceImpl")//格式化 service 实现类文件名称
                            //Entity 策略配置
                            .entityBuilder()
                            .enableChainModel()//开启链式模型
                            .enableLombok()//开启Lombok模型
                            .enableTableFieldAnnotation()//开启生成实体时生成字段注解
                            //.logicDeleteColumnName("deleted")//默认删除属性名称（数据库）
                            //.logicDeletePropertyName("deleted")//默认删除属性名称（实体）
                            //.versionColumnName("version")//乐观锁属性名（数据库）
                            //.versionPropertyName("version")//乐观锁属性名（实体）
                            //.addTableFills(new Column("create_Time", FieldFill.INSERT))//添加表字段填充(自动填充)
                            //.addTableFills(new Column("update_Time", FieldFill.INSERT_UPDATE))//添加表字段填充（自动填充）
                            //controller 策略配置
                            .controllerBuilder()
                            .enableRestStyle();//开启生成@RestController 控制器
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
