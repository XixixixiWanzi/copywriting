<#assign className = "${tableInfo.entityName}">
<#assign lowerName = "${tableInfo.entityNameFirstLower}">
package ${packageConfig.parent}.${moduleName}.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
* <p>
    * ${tableInfo.comment} 实体类
    * </p>
*/
@Data
public class ${className} {

<#list tableInfo.fields as field>
    <#if field.keyIdentity>
        @TableId(value = "${field.name}", type = IdType.AUTO)
    <#else>
        @TableField("${field.column}")
    </#if>
    private ${field.propertyType} ${field.propertyName};

</#list>
}
