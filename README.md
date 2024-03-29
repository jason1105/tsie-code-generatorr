
# TSIE Code Generator

> 一个依赖 IDEA DatabaseTools 的代码生成器，通过数据库表结构生成相应的Java代码，插件提供一套简单的增删查改代码模板，也可以新增自定义模板来生成前端代码或其他相关的代码。

本插件支持 `beetl`/`freemarker`/`velocity` 三种模板引擎的代码模板，通过文件后缀(`btl`/`ftl`/`vm`)来选择使用什么模板引擎渲染，自定义模板请阅读 [模板变量文档](./doc/template-document.md) 了解相关变量内容后再进行自定义开发。



[模板变量说明](./doc/template-document.md) | [插件截图](./doc/images.md) | [所有变量使用 all-variable.ftl](src/main/resources/templates/all-variable.ftl)



默认提供三种模板引擎（`beetl`/`freemarker`/`velocity`）的代码模板（SpringBoot+MyBatis-Plus+自定义工具），可选择保留其中一种模板引擎的代码模板，然后根据自己的需求对代码模板进行修改，阅读 [模板变量说明](./doc/template-document.md) 了解模板变量的具体内容。



## 代码模板文件在IDEA中存放的位置

- 代码模板文件默认放到：`Scratches and Consoles/Extensions` （中文：草稿文件和控制台/扩展/Database Generator）
- 同时支持以下模板路径：`${project.dir}/.idea/generator/templates` 和 `${project.dir}/generator/templates`



## 截图

![](./doc/assets/images_1.png)

![img.png](doc/assets/img.png)

![](./doc/assets/images_8.png)

![](./doc/assets/images_9.png)

![](./doc/assets/images_10.png)

![](./doc/assets/images_11.png)



