# Store代码目录
<br/>
## core   核心库
&emsp;&emsp;抽取一些公用的类及工具类
###src
&emsp;&emsp;源码及资源目录
####main
&emsp;&emsp;项目主目录
#####java
&emsp;&emsp;java类文件
######com
&emsp;&emsp;根包
#######core
&emsp;&emsp;核心包，定义一些公用的java类<br/>
###pom.xml
&emsp;&emsp;pom依赖包及插件配置文件

## doc 资源文件目录
&emsp;&emsp;存放了设计文档和权限资源文件
### design 设计文档目录
### resource 权限资源目录（在角色权限上传资源文件中使用）

## store-app 官网前台
### src/main/java 代码目录
#### controller 控制器
&emsp;&emsp;1. AboutUsController 
#### taglib  sitemesh的自定义标签库
### src/main/resources 资源文件目录
&emsp;&emsp;1.applicationContext.xml spring容器配置文件<br/>
&emsp;&emsp;2.log4j.properties 日志配置文件<br/>
&emsp;&emsp;3.store.properties 主要用于运行时的数据库配置信息(由pom.xml中指定的profile决定)<br/>
&emsp;&emsp;4.version。properties 项目的版本信息<br/>

## store-server 官网后台管理系统

## pom.xml 项目的maven配置文件
&emsp;&emsp; 配置依赖库的版本及各模块的定义
