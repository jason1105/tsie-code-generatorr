# README.md

## ZH-CN
本插件默认提供三种模板引擎的参照模板代码，在正式使用时，请选择想要使用的一种模板引擎的模板代码保留，然后删除其他模板引擎的模板代码。

代码模板变量参数信息请查看 [模板变量用法](http://git.tsintergy.com:8070/ssc/code-generator/tsie-code-generator/blob/master/doc/template-document.md)

2.5.0 版本默认会在 `Scratches and Consoles - Extensions` 路径下创建代码模板和配置文件，不会在 `${project.dir}/generator` 存放相关文件。

支持以下路径的代码模板：

1. `${project.dir}/.idea/generator/templates`
2. `${project.dir}/generator/templates`
3. `Scratches and Consoles - Extensions/Plugin/generator/templates`
