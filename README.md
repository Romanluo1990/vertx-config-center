# vertx-config-center
简单的基于vertx配置中心  

---
一般项目中很多配置属性写在配置文件里，线上环境如果需要动态更改配置需要重启服务，如果时集群需要全部修改重启，不是很方便，通过配置中心可以方便快速维护下发配置


客户端启动：
修改application.properties配置  

```
#配置中心域
applation.domain=test
#配置中心zk
applation.zoo=zoo1:2181

#测试用属性
white_list=张三

```
