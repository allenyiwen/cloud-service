// 我们这里demo是直接访问网关的，因此domainName配置的是后端java服务网关层的域名和端口，
// 正式生产为了保证网关的高可用性，肯定是部署了多个网关服务，然后用nginx反向代理的
// 那么多个网关服务或者生产环境的话，我们这里配置的是nginx的地址
var domainName = "http://api.gateway.com:8080";


// 登陆页地址，未登录或过期时进行跳转，如果是前端单独部署的话，这里请写全路径，如http://xx.xx.xx/login.html
var loginPage = "/api-b/login.html";