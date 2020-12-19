# cloud-service
## 2019.01.07 添加阿里云文件存储oss，重构文件中心上传代码
## 2019.01.09
1. 添加通知中心notification-center，引入阿里云短信，发送短信验证码
2. 用户中心添加用户凭证表，添加绑定手机号功能
3. 网关层重构登陆接口，支持用户名+密码、手机号+密码、手机号+短信验证码登陆
4. 认证中心UserDetailsService重构，使其支持手机号+短信验证码登陆
## 2019.01.16 日志中心增加实现类EsLogServiceImpl，支持将日志存储到elasticsearch
## 2019.01.28 用户中心增加微信授权，zuul和认证中心添加微信登陆处理
## 2019.02.04 将启动类上的注解@EnableEurekaClient换为@EnableDiscoveryClient
1. 两个注解效果一样，前者只试用于eureka当注册中心，后者兼容的多些
## 2019.02.08 作废pom依赖修改
1. spring-cloud-starter-eureka改为spring-cloud-starter-netflix-eureka-client
2. spring-cloud-starter-eureka-server改为spring-cloud-starter-netflix-eureka-server
3. spring-cloud-starter-feign改为spring-cloud-starter-openfeign
## 2019.02.12 引入zipkin追踪
1. 文档见05 zipkin追踪.docx
2. 相关服务pom里加入了spring-cloud-starter-zipkin，全局搜下即可找到
3. 配置加入了spring.zipkin.base-url=http://localhost:9411,注意我们是yml格式
## 2019.02.17 将AppUser类的字段enabled类型由boolean改为Boolean
## 2019.02 spring boot由1.x升级到2.x
1. 认证中心sql脚本，表oauth_client_details的字段client_secret由明文改为密文,刷下cloud_oauth.sql即可
2. 文件中心配置file-center.yml参数由spring.http.multipart改为spring.servlet.multipart
3. 认证中心配置oauth-center.yml去除security.oauth2.resource.filter-order
4. 数据库连接池2.x改为hikari,在各个服务的数据库配置里
5. 各个服务bootstrap.yml里的参数management.security.enabled调整了,具体看配置
## 2019.02.18 调整删除黑名单ip的url，将ip从路径调整到参数
## 2019.02.06 优化认证中心
1. 添加实现类RedisClientDetailsService，继承JdbcClientDetailsService，在数据库的基础上做了redis缓存
2. 修改表oauth_client_details的数据时，请注意清除redis缓存，是hash结构，key是client_details
## 2019.02.19 manage-backend调整main.js里菜单渲染和我的菜单接口，支持多级菜单
## 2019.03.10 升级spring boot和cloud的pom版本，并修复使用jwt方式时的一些bug
## 2019.03.11 右上角当前登录用户头像下拉菜单添加修改密码页面
## 2019.03.12 添加client管理功能
1. api-model添加了SystemClientInfo、修改了LogModule，认证中心添加了ClientController
2. 页面新增了pages/client下的几个文件，cloud_user.sql权限、角色权限关系数据增加，cloud_backend.sql菜单、角色和菜单关系数据增加
## 2019.03.13 认证中心添加参数access_token.add-userinfo
1. 该参数意在控制登陆接口返回的数据是否追加当前用户信息，默认false
2. 改动了认证中心的类AuthorizationServerConfig，添加方法addLoginUserInfo
## 2019.03.15 部分服务的bootstrap.yml里添加参数spring.cloud.config.fail-fast=true
1. 部分服务指的是那些将配置文件放在config-center的服务
2. 参数的意思是启动服务时如果需要从配置中心拉取配置，但未找到配置中心服务话，启动立马失败
## 2019.03.20
1. 作废LogModule，各个服务里用到日志注解@LogAnnotation的，module由常量调整为汉字
2. 日志查询logList.html里调整模块下拉框为输入框
3. 日志服务里的查询语句由等于改为模糊匹配
4. 表t_log的老数据可运行下测试类LogTest生成修改sql
5. 认证中心pom引入log-starter依赖，oauth-center.yml引入rabbitmq配置
6. 修改AppUserUtil中获取当前登录用户的逻辑，主要是认证中心和别的服务逻辑不同
7. 工程log-starter里添加类LogMqClient，Autowired注入该类即可在方法内存储日志
## 2019.03.29 解决每次登陆返回同一个access_token问题（非常重要，务必更新）
1. 认证中心添加类RandomAuthenticationKeyGenerator
2. 认证中心中修改AuthorizationServerConfig里的方法tokenStore()
# cloud-service
