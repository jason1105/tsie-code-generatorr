
一个依赖 IDEA Database 工具的代码生成器，通过数据库表结构生成相应的Java代码，插件提供一套简单的增删查改代码模板，也可以新增自定义模板来生成前端代码或其他相关的代码。

提供 `all-variable.ftl` 模板文件来说明所有的变量使用和对应结果。

本插件支持 `beetl`/`freemarker`/`velocity` 三种模板引擎的代码模板，通过文件后缀(`btl`/`ftl`/`vm`)来选择使用什么模板引擎渲染，自定义模板请查看 <a href="https://github.com/houkunlin/Database-Generator/blob/master/doc/template-document.md">模板变量文档</a> ，或查看代码模板 <a href="https://github.com/houkunlin/Database-Generator/blob/master/src/main/resources/templates/all-variable.ftl">all-variable.ftl</a>


本插件基于开源项目 <a href="https://github.com/houkunlin/Database-Generator">Database-Generator</a> 修改, 在此表示感谢.

Email: lvwei@tsintergy.com
