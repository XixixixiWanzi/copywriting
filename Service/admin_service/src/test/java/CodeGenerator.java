

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;


public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/copywriting_system?serverTimezone=GMT%2B8&characterEncoding=utf-8&userSSL=false", "root", "LSJ060195711")
                .globalConfig(builder -> {
                    builder.author("XixixixiWanzi") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\Users\\86181\\文档\\山东师范大学\\java\\CopywritingSystem\\Service\\admin_service\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.example") // 设置父包名
                            .moduleName("demo") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:/Users/86181/文档/山东师范大学/java/CopywritingSystem/Service/admin_service/src/main/java/com/example/demo/mapper/xml")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("dialogue","template")// 设置需要生成的表名
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .versionColumnName("version")
                            .logicDeleteColumnName("deleted")

                            .controllerBuilder()
                            .enableRestStyle();


                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
