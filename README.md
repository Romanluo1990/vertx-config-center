# vertx-config-center
简单的基于vertx配置中心  

---
一般项目中很多配置属性写在配置文件里，线上环境如果需要动态更改配置需要重启服务，如果时集群需要全部修改重启，不是很方便，通过配置中心可以方便快速维护下发配置，本项目基于vertx实现配置中心，客户端会依次按：远程服务端配置-》app环境变量-》系统环境变量-》本地配置文件 优先级获取配置属性

## 模块说明  

cfgcenter-core：基础模块  
cfgcenter-client：客户端模块  
cfgcenter-server：服务端模块  
cfgcenter-api：rest api模块  

# 外部依赖

1.mysql表`config`,用来落地配置数据
```
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `domain` varchar(128) NOT NULL COMMENT '配置域',
  `key` varchar(64) NOT NULL COMMENT '配置key',
  `value` varchar(255) NOT NULL COMMENT '配置value',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_domain_key` (`domain`,`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置表';
```

2.zookeeper用来管理集群

## 客户端启动(cfgcenter-client)  

1.修改application.properties配置  
```
#配置中心域
applation.domain=test
#配置中心zk
applation.zoo=zoo1:2181

#测试用属性
white_list=张三

```
2.启动客户端测试用例  
roman.common.cfgcenter.client.PropertyManagerTest#main  

## api服务端启动(cfgcenter-api)  

1.修改application.properties配置 
```
#配置中心域
cfgcenter.domain=configcenter.server
#配置中心zk
cfgcenter.zoo=zoo1:2181

#datasource.hikari
datasource.hikari.driverClassName=com.mysql.cj.jdbc.Driver
datasource.hikari.jdbcUrl=jdbc:mysql://localhost:3306/mydb?autoReconnect=true&autoReconnectForPools=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&useLegacyDatetimeCode=false&serverTimezone=GMT%2b8
datasource.hikari.username=root
datasource.hikari.password=123456
datasource.hikari.maximumPoolSize=60
datasource.hikari.connectionTimeout=60000
datasource.hikari.minimumIdle=10
```
2.启动服务端  
roman.common.cfgcenter.Application#main  

## 测试
服务端启动后可直接访问：http://localhost:8080/swagger-ui.html快速调用rest 接口测试  

增加配置  
curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' -d '{ \ 
   "domain": "test", \ 
   "key": "white_list", \ 
   "value": "张三，李四", \ 
   "desc": "白名单" \ 
 }' 'http://localhost:8080/config'
 
客户端日志输出结果：  
2019-05-28 17:48:47.347 INFO  roman.common.cfgcenter.client.PropertyManagerTest Line:18  - white_list: 张三  
2019-05-28 17:48:48.503 INFO  roman.common.cfgcenter.client.PropertyManagerTest Line:14  - property white_list update to: 张三，李四  
2019-05-28 17:48:50.351 INFO  roman.common.cfgcenter.client.PropertyManagerTest Line:18  - white_list: 张三，李四
