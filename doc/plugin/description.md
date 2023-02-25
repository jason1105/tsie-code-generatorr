
一个依赖 IDEA Database 工具的代码生成器，通过数据库表结构生成相应的Java代码，插件提供一套简单的增删查改代码模板，也可以新增自定义模板来生成前端代码或其他相关的代码。

<br>

提供 `all-variable.ftl` 模板文件来说明所有的变量使用和对应结果。

<br>

本插件支持 `beetl`/`freemarker`/`velocity` 三种模板引擎的代码模板，通过文件后缀(`btl`/`ftl`/`vm`)来选择使用什么模板引擎渲染，自定义模板请阅读 [模板变量文档](../template-document.md) 了解相关变量内容后再进行自定义开发。

<br>

**请查看详细的编写 [模板变量文档](../template-document.md) ，或者查看涵盖所有变量使用的代码模板 [all-variable.ftl](../../src/main/resources/templates/all-variable.ftl)**

<br><br>
Author: TSIE-SSC<br>
Email: lvwei@tsintergy.com
