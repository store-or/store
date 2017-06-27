# Store代码目录
<br/>
## core   核心库
&emsp;&emsp;抽取一些公用的类及工具类
###src/main/java &emsp;&emsp;java类文件目录
-- com 根包<br/>
----- core 核心包(这里会定义出一些常用的工具包、基类，具体不做解释。基本不需要变动)<br/>
----- param 参数模块包(这里也不具体描述，基本不修改)<br/>
----- store 商城业务包(服务于server及app)<br/> 
--------- domain 实体类包(数据表映射)<br/>
------------- banner banner包(banner实体及枚举类型定义)<br/>
----------------- BannerDO (banner的实体类)<br/>
----------------- BannerType (banner类型的枚举)<br/>
------------- product 产品包(产品实体及枚举类型定义)<br/>
----------------- ProductDetailDO (产品详情实体类)<br/>
----------------- ProductDO (产品信息实体类)<br/>
----------------- Status (产品状态枚举类)<br/>
----------------- Tag (产品标签枚举类)<br/>
--------------BaseIndexDO (含有index字段的实体基类)<br/>
--------------ClassifyDO (产品分类实体类)<br/>
--------------FileInfo (文件上传的实体类)<br/>
--------------MenuDO (关于我们的菜单实体类)<br/>
--------------RecommendDO (推荐位的实体类)<br/>
--------------UeditorInfoDO (编辑器实体类)<br/>
--------- exception 异常类包(业务异常)<br/>
--------------CustomExceptionHandler (spring的异常处理类)<br/>
--------------StoreException (业务异常类)<br/>
--------- service 逻辑类包(与dao交互)<br/>
--------------BannerService (Banner的业务逻辑类)<br/>
--------------BaseIndexService (含有index的实体业务逻辑基类)<br/>
--------------ClassifyService (分类业务逻辑类)<br/>
--------------MenuService (关于我们菜单的业务逻辑类)<br/>
--------------ProductService (产品业务逻辑类)<br/>
--------------RecommendService (推荐位业务逻辑类)<br/>
--------- system 系统初始化类包<br/>
--------------HttpJobInit (httpClinet的初始化类)<br/>
--------------StoreApp (系统初始化的入口类)<br/>
--------------StoreConfig (不可变属性的配置类)<br/>
--------------StoreConfigInit (配置初始化类)<br/>

###pom.xml
&emsp;&emsp;pom依赖包及插件配置文件

## doc 资源文件目录
&emsp;&emsp;存放了设计文档和权限资源文件
### design 设计文档目录
-	readme.md markdown语法的代码说明文档
-	数据库设计.txt 数据库粗略设计文档
-	设计.docx 功能模块设计及说明
### resource 权限资源目录（在角色权限上传资源文件中使用）
-	banner.xml banner的功能权限
-	classify.xml 产品分类的功能权限
-	companyConfig.xml 页面配置功能权限
-	menu.xml 关于我们菜单功能权限
-	privilege.xml 系统管理功能权限
-	product.xml 产品功能权限
-	recommend.xml 推荐位功能权限
## store-app 官网前台
### src/main/java 代码目录
#### controller 控制器
-	1. AboutUsController  关于我们的菜单控制器 
-	2. BaseController     基类控制器
-	3. ContactUsController 联系我们控制器
-	4. IndexController  首页控制器
-	5. ParamReloadController 参数重新加载控制器(用于后台编辑完参数，刷新前端的参数值)
-	6. ProductController 产品页面控制器
#### taglib  sitemesh的自定义标签库
----PathTagRuleBundle sitemesh的标签库类
### src/main/resources 资源文件目录
-	1.applicationContext.xml spring容器配置文件<br/>
-	2.log4j.properties 日志配置文件<br/>
-	3.store.properties 主要用于运行时的数据库配置信息(由pom.xml中指定的profile决定)<br/>
-	4.version。properties 项目的版本信息<br/>
### src/main/webapp 前端资源
&emsp;&emsp;包含图片、js、css、页面等文件<br/>
--css 样式文件<br/>
--img 图片文件<br/>
--js js文件<br/>
--WEB-INF 前台页面及配置文件<br/>
-----content 页面文件目录<br/>
--------decorators sitemesh装饰器目录<br/>
-----------default_decorator.ftl 商城页头与页尾等基础内容定义<br/>
--------exception 错误页面目录<br/>
-----------error.jsp 异常错误的页面<br/>
-----------error_404.jsp 未找到请求页面<br/>
-----------error_500.jsp 服务端出错页面<br/>
-----------error_503.jsp 禁止访问页面<br/>
--------store 商城页面目录<br/>
-----------about 关于我们的页面目录<br/>
--------------index.ftl 关于我们的主页面<br/>
--------------show.ftl 菜单的内容展示页面<br/>
-----------contact 联系我们的页面目录<br/>
--------------index.ftl 联系我们的页面<br/>
-----------product 产品的页面目录<br/>
--------------detail.ftl 产品详情页面<br/>
--------------list.ftl 产品列表页面<br/>
-----------display.ftl 定义一些基础操作页面<br/>
-----------index.ftl 首页<br/>
-----lib editor编辑器的依赖包<br/>
-----dispatcher-servlet.xml spring mvc的配置文件<br/>
-----sitemesh3.xml 装饰器sitemesh配置文件(使用模板)<br/>
-----web.xml web项目的配置入口文件

## store-server 官网后台管理系统
### src/main/java 代码目录
--com根包<br/>
----- param(参数配置包)<br/>
-------- PortalParamController(参数配置控制器)<br/>
----- prillege(系统管理包)<br/>
-------- controller(控制器包)<br/>
----------- ResourceController(资源控制器)<br/>
----------- RoleController(角色控制器)<br/>
----------- UserController(用户控制器)<br/>
-------- domain(实体类包)<br/>
----------- Action(资源action表的实体)<br/>
----------- ActionRole(权限资源action_role的实体)<br/>
----------- Resource(资源目录resource的实体)<br/>
----------- Role(角色role的实体)<br/>
----------- User(用户user的实体)<br/>
-------- exception(异常类包)<br/>
----------- IncorrectCaptchaException(错误的验证码异常)<br/>
----------- NotUniqueException(非唯一异常)<br/>
----------- PermissionException(没有权限异常)<br/>
----------- UserHasBeenAssignedException(用户已被分配异常)<br/>
-------- security(shiro等权限包)<br/>
----------- ActionMatch(action的匹配工具类)<br/>
----------- CustomAuthenticationFilter(shiro登录过滤器)<br/>
----------- CustomAuthorizationFilter(shiro访问功能过滤器)<br/>
----------- Principal(Session中的用户资料)<br/>
----------- ResourceCache(本地功能资源的缓存)<br/>
----------- SecurityContext(前端工具类、获取用户信息)<br/>
----------- ShiroDbRealm(shiro框架验证)<br/>
-------- service(逻辑类包)<br/>
----------- resource.template(资源导入的处理包)<br/>
-------------- ActionTemplate(资源xml模板类)<br/>
-------------- FilterType(过滤类型常量类)<br/>
-------------- MethodType(method常量类)<br/>
-------------- Parameter(参数xml模板类)<br/>
-------------- Parameters(参数列表xml模板类)<br/>
-------------- ResourceTemplate(资源目录xml模板类)<br/>
----------- ActionRoleService(权限资源的逻辑)<br/>
----------- ActionService(资源逻辑类)<br/>
----------- ResourceService(资源目录逻辑类)<br/>
----------- RoleService(角色逻辑类)<br/>
----------- UserService(用户逻辑类)<br/>
-------- system(初始化包)<br/>
----------- PrivilegeApp(系统管理初始化入口)<br/>
-------- SecurityTag(自定义标签库&lt;@sec.authorize/&gt;)<br/>
----- store(商城后台管理包)<br/>
-------- controller(控制器包)<br/>
----------- BannerController(banner管理控制器)<br/>
----------- BaseController(控制器基)<br/>
----------- ClassifyController(分类管理控制器)<br/>
----------- CompanyConfigController(页面配置控制器)<br/>
----------- FileController(文件上传控制器)<br/>
----------- HomeController(首页控制器)<br/>
----------- LoginController(登录控制器)<br/>
----------- MenuController(关于我们菜单管理控制器)<br/>
----------- ProductController(产品管理控制器)<br/>
----------- RecommendController(推荐位管理控制器)<br/>
----------- UeditorController(editor编辑器控制器)<br/>
-------- service(逻辑包)<br/>
----------- FileService(文件导入逻辑处理类)<br/>
-------- taglib(sitemesh的自定义标签库包)<br/>
------------PathTagRuleBundle sitemesh的标签库类<br/>
### src/main/resources 资源文件目录
-	1.applicationContext-shiro.xml shiro验证框架配置文件
-	2.applicationContext.xml spring容器配置文件<br/>
-	3.log4j.properties 日志配置文件<br/>
-	4.store.properties 主要用于运行时的数据库配置信息(由pom.xml中指定的profile决定)<br/>
-	5.version。properties 项目的版本信息<br/>
### src/main/webapp 前端资源
&emsp;&emsp;包含图片、js、css、页面等文件<br/>
--css 样式文件<br/>
--img 图片文件<br/>
--js js文件<br/>
--WEB-INF 前台页面及配置文件<br/>
-----content 页面文件目录<br/>
--------decorators sitemesh装饰器目录(类似于store-app中描述)<br/>
--------exception 错误页面目录(类似于store-app中描述)<br/>
--------param 系统参数页面目录<br/>
--------privilege 用户管理及角色管理页面目录(这里不再详细描述可根据目录名看出模块)<br/>
--------store 商城后台管理页面目录(具体不再描述，可通过不同目录映射出模块)<br/>
-----customTags(自定义标签库目录)<br/>
--------security.tld(自定义标签库&lt;@sec.authorize/&gt;)<br/>
-----lib editor编辑器的依赖包<br/>
-----dispatcher-servlet.xml spring mvc的配置文件<br/>
-----sitemesh3.xml 装饰器sitemesh配置文件(使用模板)<br/>
-----web.xml web项目的配置入口文件
## pom.xml 项目的maven配置文件
&emsp;&emsp; 配置依赖库的版本及各模块的定义
