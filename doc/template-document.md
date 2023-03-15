[TOC]

# 模板编写文档
模板中会初始化8个变量，其中有3个变量为配置信息变量，有5个变量为数据库相关信息变量。


新增了一个 `src/main/resources/templates/all-variable.ftl` 代码模板来说明所有变量的使用和对应的结果。



## 模板变量关系图

图中的 `RootModel` 对象的5个字段是模板代码的5个变量

![RootModel](assets/RootModel.png)



## 模板内变量说明
### `settings` 基础设置信息变量
Type: `Object` Class: `com.tsintergy.ssc.config.Settings`

|字段/方法|类型|说明|
| ----|----|----|
|`projectPath`|String|当前项目路径|
|`javaPath`|String|Java代码路径|
|`sourcesPath`|String|资源文件路径|
|`entitySuffix`|String|Entity 后缀|
|`daoSuffix`|String|Dao 后缀|
|`serviceSuffix`|String|Service 后缀|
|`controllerSuffix`|String|Controller 后缀|
|`entityPackage`|String|Entity 包名|
|`daoPackage`|String|Dao 包名|
|`servicePackage`|String|Service 包名|
|`controllerPackage`|String|Controller 包名|
|`xmlPackage`|String|Mapper XML 包名|



### `developer` 开发者信息变量

Type: `Object` Class: `com.tsintergy.ssc.config.Developer`

|字段/方法|类型|说明|
| ----|----|----|
|`author`|String|开发者姓名|
|`email`|String|开发者电子邮件|



### `gen` 当前文件类型配置对象

Type: `Object` Class: `com.tsintergy.ssc.vo.Variable`

|字段/方法|类型|说明|
| ----|----|----|
|`setFilename(String filename)`| |设置保存文件名|
|`setFilepath(String filepath)`| |设置保存文件路径|
|`setType(String type)`| |设置保存文件类型|

设置了 `type` 值后，可忽略不设置 `filename` `filepath` 值，但仅限于以下 `type` 可选值范围：

- `entity` Entity 对象
    - 默认文件名: `${entity.name}${settings.entitySuffix}.java`
    - 默认路径: `${settings.javaPath}/${settings.entityPackage}`
- `dao` Dao 对象
    - 默认文件名: `${entity.name}${settings.daoSuffix}.java`
    - 默认路径: `${settings.javaPath}/${settings.daoPackage}`
- `service` Service 接口对象
    - 默认文件名: `${entity.name}${settings.serviceSuffix}.java`
    - 默认路径: `${settings.javaPath}/${settings.servicePackage}`
- `serviceImpl` Service 实现类
    - 默认文件名: `${entity.name}${settings.serviceSuffix}Impl.java`
     - 默认路径: `${settings.javaPath}/${settings.servicePackage}.impl`
- `controller` Controller 对象
    - 默认文件名: `${entity.name}${settings.controllerSuffix}.java`
    - 默认路径: `${settings.javaPath}/${settings.controllerPackage}`
- `xml` MyBatis Mapper 文件。
    - 默认文件名: `${entity.name}${settings.daoSuffix}.xml`
    - 默认路径: `${settings.sourcesPath}/${settings.xmlPackage}`
- 其他未在以上类型列表中的值
    - 默认文件名: `${entity.name}.java`
    - 默认路径: `${settings.sourcesPath}/temp/`



### `date` 时间信息变量

该变量实际上是一个 `joda-time` 的 `DateTime` 对象，其值默认为 `DateTime.now()` 点击“生成代码”按钮那一刻的时间。

只要把该变量当做 `joda-time` 的 `DateTime` 对象来使用就行了，示例：

- `${date.toString("yyyy-MM-dd HH:mm:ss")}` 该返回结果一直是点击“生成代码”按钮那一刻的时间，也就是在生成代码的过程中得到的这个时间是不变的
- `${date.now().toString("yyyy-MM-dd HH:mm:ss")}` 能够得到最新的时间信息，也就是渲染模板时的当前时间信息



### `table` 数据库表信息变量

Type: `Object` Class: `com.tsintergy.ssc.vo.impl.TableImpl` 实现了 `com.tsintergy.ssc.vo.ITable` 接口

|字段/方法|类型|说明|
| ----|----|----|
|`name`|String|数据库表名名称|
|`comment`|String|数据库表注释内容|
|`dbTable`|com.intellij.database.psi.DbTable|开发工具的内部对象，不建议使用|



### `columns` 数据库表字段列表变量

Type: `List` Class: `com.tsintergy.ssc.vo.impl.TableColumnImpl` 实现了 `com.tsintergy.ssc.vo.ITableColumn` 接口

|字段/方法|类型|说明|
| ----|----|----|
|`field`|IEntityField|该字段为当前表字段对应的Java字段对象|
|`name`|String|列名|
|`comment`|String|列注释|
|`typeName`|String|列类型信息（短）|
|`fullTypeName`|String|列类型信息（完整）|
|`primaryKey`|boolean|是否是主键|
|`selected`|boolean|是否选中（通过UI勾选）|
|`dbColumn`|com.intellij.database.model.DasColumn|开发工具的内部对象，不建议使用|



### `entity` 实体类信息变量

Type: `Object` Class: `com.tsintergy.ssc.vo.impl.EntityImpl` 实现了 `com.tsintergy.ssc.vo.IEntity` 接口

|字段/方法|类型|说明|
| ----|----|----|
|`name`|EntityName<br>(本文底部)|实体名称对象|
|`comment`|String|实体注释对象（可通过UI修改）|
|`uri`|String|库表对应的请求API前缀（可通过UI修改）|
|`packages`|EntityPackage<br/>(本文底部)|实体字段中需要引入的包信息。直接使用该变量将返回Java导入包的代码|



### `fields` 实体对象字段列表信息变量

Type: `List` Class: `com.tsintergy.ssc.vo.impl.EntityFieldImpl` 实现了 `com.tsintergy.ssc.vo.IEntityField` 接口

|字段/方法|类型|说明|
| ----|----|----|
|`column`|ITableColumn|该字段为当前Java字段对应的数据库表字段对象|
|`name`|FieldNameInfo<br/>(本文底部)|字段/方法对象。直接使用该对象将调用 toString() 方法返回驼峰格式、首字母小写的字段字符串信息|
|`comment`|String|字段注释（可通过UI修改）|
|`typeName`|String|字段类型（短）|
|`fullTypeName`|String|字段类型（完整）|
|`primaryKey`|boolean|是否是主键|
|`selected`|boolean|是否选中（通过UI勾选）|



### `primary` 主键Java字段、数据库列对象信息变量

Type: `Object` Class: `com.tsintergy.ssc.vo.impl.PrimaryInfo` 

| 字段/方法 | 类型                     | 说明                                                         |
| --------- | ------------------------ | ------------------------------------------------------------ |
| `field`   | IEntityField             | 默认主键Java字段对象，如果没有主键将返回一个默认的“id”主键   |
| `column`  | ITableColumn             | 默认主键数据库字段对象，如果没有主键将返回一个默认的“id”主键 |
| `fields`  | List&lt;IEntityField&gt; | 主键Java字段列表（所有的主键列表）                           |
| `columns` | List&lt;ITableColumn&gt; | 主键数据库字段列表（所有的主键列表）                         |



## 模板其他相关对象说明

### `IName` 名称接口
Class: `com.tsintergy.ssc.vo.IName`

|字段/方法|类型|说明|
| ----|----|----|
|`toString()`|String|直接使用该对象将调用 toString() 方法，该方法根据不同使用场景将返回不同的值，主要区别在首字母是否大小写问题|
|`firstLower/getFirstLower()`|String|名称首字母小写，beetl模板调用get方法可能会出现异常|
|`firstUpper/getFirstUpper()`|String|名称首字母大写，beetl模板调用get方法可能会出现异常|



### `EntityName` 实体类名称对象（不含后缀）

Class: `com.tsintergy.ssc.vo.impl.EntityName` 实现了 `com.tsintergy.ssc.vo.IName` 接口

|字段/方法|类型|说明|
| ----|----|----|
|`value`|String|不含Entity后缀的实体类名称，也就是UI中输入框的Entity名称|
|`firstUpper`|String|同 `value` 值|
|`firstLower`|String|返回实体类首字母小写|
|`toString()`|String|返回 `value` 字段|
|`entity`|EntityNameInfo|Entity 对象名称，`toString()` 返回包含Entity后缀的名称（驼峰格式、首字母大写）|
|`service`|EntityNameInfo|Service 对象名称，`toString()` 返回包含Service后缀的名称（驼峰格式、首字母大写）|
|`serviceImpl`|EntityNameInfo|ServiceImpl 对象名称，`toString()` 返回包含ServiceImpl后缀的名称（驼峰格式、首字母大写）|
|`dao`|EntityNameInfo|Dao 对象名称，`toString()` 返回包含Dao后缀的名称（驼峰格式、首字母大写）|
|`controller`|EntityNameInfo|Controller 对象名称，`toString()` 返回包含Controller后缀的名称（驼峰格式、首字母大写）|



### `EntityNameInfo` 实体类名称信息对象（含后缀）

Class: `com.tsintergy.ssc.vo.impl.EntityNameInfo` 实现了 `com.tsintergy.ssc.vo.IName` 接口

| 字段/方法    | 类型   | 说明                                       |
| ------------ | ------ | ------------------------------------------ |
| `value`      | String | 含后缀的实体类名称（首字母大写，驼峰风格） |
| `firstUpper` | String | 同 `value` 值                              |
| `firstLower` | String | 对 `value` 的首字母小写                    |
| `toString()` | String | 同 `value` 值                              |



### `EntityPackage` 实体类包对象

Class: `com.tsintergy.ssc.vo.impl.EntityPackage`

|字段/方法|类型|说明|
| ----|----|----|
|`list`|HashSet&lt;String&gt;|Entity 字段需要导入的包列表，不含`import`字符串|
|`toString()`|String|直接使用该对象将会调用 toString() 方法返回Java语言的导入包代码（导入list中的所有包，含`import`字符串）|
|`entity`|EntityPackageInfo|Entity 包信息|
|`service`|EntityPackageInfo|Service 包信息|
|`serviceImpl`|EntityPackageInfo|ServiceImpl 包信息|
|`dao`|EntityPackageInfo|Dao 包信息|
|`controller`|EntityPackageInfo|Controller 包信息|



### `EntityPackageInfo` 实体类包信息对象

Class: `com.tsintergy.ssc.vo.impl.EntityPackageInfo`

|字段/方法|类型|说明|
| ----|----|----|
|`pack`|String|Entity/Service/Controller/Dao 对象所在包的包名称|
|`toString()`|String|返回 `pack` 字段|
|`full`|String|对象完整的包路径，即：包名称+对象名称|



### `FieldNameInfo` 字段名称信息对象

Class: `com.tsintergy.ssc.vo.impl.FieldNameInfo` 实现了 `com.tsintergy.ssc.vo.IName` 接口

| 字段/方法    | 类型   | 说明                           |
| ------------ | ------ | ------------------------------ |
| `value`      | String | 字段名称，驼峰风格，首字母小写 |
| `firstUpper` | String | 对 `value` 值的首字母大写      |
| `firstLower` | String | 同 `value` 值                  |
| `toString()` | String | 同 `value` 值                  |



## 示例代码

数据库表名：`user_info`

|代码|返回示例|
|---|---|
|`${table.name}`|user_info|
|`${entity.name}`|UserInfo|
|`${entity.name.firstLower}`|userInfo|
|`${entity.name.entity}`|UserInfoEntity|
|`${entity.name.entity.firstLower}`|userInfoEntity|
|`${entity.name.service}`|UserInfoService|
|`${entity.name.service.firstLower}`|userInfoService|
|`${entity.name.serviceImpl}`|UserInfoServiceImpl|
|`${entity.name.serviceImpl.firstLower}`|userInfoServiceImpl|
|`${entity.packages}`|`import java.math.BigDecimal;import java.util.Date;`|
|`${entity.packages.list}`|\["java.math.BigDecimal","java.util.Date"\]|
|`${entity.packages.entity}`|com.example.entity|
|`${entity.packages.entity.pack}`|com.example.entity|
|`${entity.packages.entity.full}`|com.example.entity.UserInfoEntity|
|`${entity.packages.service}`|com.example.service|
|`${entity.packages.service.pack}`|com.example.service|
|`${entity.packages.service.full}`|com.example.service.UserInfoService|
|`${entity.packages.serviceImpl}`|com.example.service.impl|
|`${entity.packages.serviceImpl.pack}`|com.example.service.impl|
|`${entity.packages.serviceImpl.full}`|com.example.service.impl.UserInfoServiceImpl|



字段名称：`user_address` 类型：`varchar(255)`

|代码|返回示例|
|---|---|
|`${field.name}`|userAddress|
|`${field.name.firstLower}`|userAddress|
|`${field.name.firstUpper}`|UserAddress|
|`${field.typeName}`|String，该值将根据 `types.json` 配置决定|
|`${field.fullTypeName}`|java.lang.String，该值将根据 `types.json` 配置决定|
|`${column.name}`|user_address|
|`${column.typeName}`|varchar|
|`${column.fullTypeName}`|varchar(255)|
