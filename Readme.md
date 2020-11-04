# 并发的解决方案

## 一、背景

学java大抵快一年了，从环境部署到框架搭建，当自己的知识面到达了一定的地步后，之前觉得玄之又玄不敢碰的高并发问题就堂而皇之的摆在眼前了。这里整理一下如何利用virtualBox和docker搭建一个微服务集群自己玩。

未来可期，能走多远，我也不知道，但我知道自己的方向，所以每走一步，都会比上一步更远。。

### 1. 突发大流量请求问题

#### 限流：

使用nginx的限流或gateway的限流操作将并发数限制在可承受范围，多余的请求将返回降级后的信息，如

503，当前访问人数过多，请稍后再试。

#### 消息队列：

使用kafka将请求的消息存入消息队列，进行缓冲，再由消费者依次调用相关的处理程序进行处理

### 2. 分布式锁

秒杀扣库问题

### 3. 表单重复提交问题

保证kafka幂等性

### 4. 分布式事务

一个服务失败所有服务回滚并给出相关提示

### 5. 服务熔断与降级的操作

并发数量过大，返回降级信息

服务突然挂掉，熔断该服务



## 二、服务搭建

### 1. 基础环境搭建

#### 工具：

镜像： CentOS-7-x86_64-DVD-2003.iso   **（注意！centos一定要版本7以上，否则没法玩）**

VirtualBox：https://www.virtualbox.org/wiki/Downloads

#### 创建与配置虚拟机

![image-20201101151447246](./资源/image-20201101151447246.png)

点击新建，然后按照提示操作，分别输入虚拟机名称，数据将要保存到的文件夹，类型选择linux，版本看你（此处我是redhat x64）然后点击下一步

![image-20201101151509289](./资源/image-20201101151509289.png)

为虚拟机分配内存，依据你的要求来做，我这里做了一个放nacos，一个放数据（redis+mysql），一个放服务

![image-20201101151734981](./资源/image-20201101151734981.png)

创建硬盘，除了手动修改一下你想要的硬盘大小外全部使用默认配置即可，一路下一步

![image-20201101151757790](./资源/image-20201101151757790.png)

右键自己创建好的虚拟机，点击设置，在弹窗选择网络，选择桥接网卡，如果界面名称显示不出东西，那么你需要安装一个驱动，详见下面的[无法找到桥接网卡的办法](#1. 虚拟机桥接网卡的问题，找不到桥接网卡解决方案：)

![image-20201101152111228](./资源/image-20201101152111228.png)

设置好后点击确定进入虚拟机安装centos（在使用了docker技术后，理论上是不需要我们多个虚拟机操作了的，但是为了，但是为了联系多台机器的部署，我还是开了三台虚拟机，分别安装centos）。

#### 安装centos

双击virtualBox界面中自己的虚拟机实例就可以打开，下一步就是选择一个镜像进行读取，当然也可以通过右键设置-存储进行注册光驱

![image-20201101152647106](./资源/image-20201101152647106.png)



![image-20201101153209878](./资源/image-20201101153209878.png)

一开始界面中是没有光盘的，需要点击注册，然后选择你的镜像文件即可

![image-20201101153236824](./资源/image-20201101153236824.png)

选择后虚拟机将自动进入安装界面，按提示操作即可。首先选择中文，再选择硬盘，设置登录密码，一路往下即可

![image-20201101153437307](./资源/image-20201101153437307.png)

选择安装 centos等待

![image-20201101153525853](./资源/image-20201101153525853.png)

选择中文，继续

![image-20201101153556325](./资源/image-20201101153556325.png)

选择硬盘

![image-20201101153638609](./资源/image-20201101153638609.png)

![image-20201101153713405](./资源/image-20201101153713405.png)

点击完成出去点击安装，等待的同时可以设置一下root密码，否则无法完成安装

![image-20201101153756847](./资源/image-20201101153756847.png)

![image-20201101153817185](./资源/image-20201101153817185.png)

安装完毕后，接下来就是配置固定IP，参考 [完成安装后，配置固定IP](#2. 为虚拟机设置固定IP)

至此，前期准备已经完成，JDK？不存在的，docker好就好在你连JDK都不用装，直接去用就行了，而且更棒的是，人家甚至还内置cenos，可以理解成套娃，嗯。

接下来就是部署各种基础工具了。

### 2. 安装docker

~~~shell
#yum 环境安装
yum -y install yum-utils
#添加镜像加速
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://8q0juicq.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
#加一个镜像地址
sudo yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo 
#直接安装
sudo yum install -y docker-ce
#等待，完事儿
service docker start
#（可选）设置docker开机自启动
systemctl enable docker
~~~

更多高端docker操作比如自己打包镜像，后续我会慢慢放上来

### 3. 部署mysql

注意：此处务必到nacos的官方pom下看看人家支持的是哪个版本的mysql

这里是8.0.15，版本选的太高nacos不支持会报神奇的错误

比如8.0.16mysql报了神奇的错误，容器启动失败，无奈折腾了很久就是搞不定，使用了8.0.15完美解决

~~~shell
#简简单单，先找mysql
docker search mysql
#选取第一个官方镜像
docker pull mysql:8.0.16
#下载完后使用这个镜像新建一个mysql容器
docker run --name mysql -p 3306:3306 -v /data/mysql/config:/etc/mysql/conf.d -v /data/mysql/log:/logs -v /data/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=williamworkstation -d mysql
#注意，这里不知道为什么不能加--restart=always，加了以后容器闪退，只能执行完容器创建后重启容器，对想要加上开机自启动的容器使用如下代码配置自启动，目前还不知道为什么
docker update --restart=always [容器id]
#确认开放对应端口，使用navicat测试连接即可

~~~

### 4. 部署redis

~~~shell
#简简单单，先找redis
docker search redis
#选取第一个官方镜像
docker pull redis
#下载完后使用这个镜像新建一个redis容器
docker run -p 6379:6379 --name redis -v /data/redis/configs:/etc/redis/redis.conf -v /data/redis/data:/data -d redis redis-server /etc/redis/redis.conf --requirepass "williamworkstation"
#注意，这里不知道为什么不能加--restart=always，加了以后容器闪退，只能执行完容器创建后重启容器，对想要加上开机自启动的容器使用如下代码配置自启动，目前还不知道为什么
docker update --restart=always [容器id]
#确认开放对应端口，使用redis工具测试链接即可

~~~



### 5. 部署nacos注册中心

[nacos-mysql初始化脚本地址]: https://github.com/alibaba/nacos/blob/master/config/src/main/resources/META-INF/nacos-db.sql

~~~shell
#简简单单，先找nacos
docker search nacos
#选取第一个官方镜像
docker pull nacos/nacos-server
#下载完后使用这个镜像新建一个nacos容器
docker run -d -p 8848:8848 -e MODE=standalone -v /data/nacos/configs:/home/nacos/init.d -v /data/nacos/logs:/home/nacos/logs --name nacos nacos/nacos-server
#注意，这里不知道为什么不能加--restart=always，加了以后容器闪退，只能执行完容器创建后重启容器，对想要加上开机自启动的容器使用如下代码配置自启动，目前还不知道为什么
docker update --restart=always [容器id]
#在mysql中执行nacos的脚本
#脚本执行完毕后，进行nacos的配置,创建配置文件custom.properties
#配置完后重启docker容器等一段时间，nacos启动有点慢，我的机器启动大概花了1分钟

~~~

~~~shell
server.contextPath=/nacos
server.servlet.contextPath=/nacos
server.port=8848

spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://xx.xx.xx.x:3306/nacos_devtest_prod?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=user
db.password=pass


nacos.cmdb.dumpTaskInterval=3600
nacos.cmdb.eventTaskInterval=10
nacos.cmdb.labelTaskInterval=300
nacos.cmdb.loadDataAtStart=false

management.metrics.export.elastic.enabled=false

management.metrics.export.influx.enabled=false


server.tomcat.accesslog.enabled=true
server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D %{User-Agent}i


nacos.security.ignore.urls=/,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/v1/auth/login,/v1/console/health/**,/v1/cs/**,/v1/ns/**,/v1/cmdb/**,/actuator/**,/v1/console/server/**
nacos.naming.distro.taskDispatchThreadCount=1
nacos.naming.distro.taskDispatchPeriod=200
nacos.naming.distro.batchSyncKeyCount=1000
nacos.naming.distro.initDataRatio=0.9
nacos.naming.distro.syncRetryDelay=5000
nacos.naming.data.warmup=true
nacos.naming.expireInstance=true
~~~





![image-20201101162202310](./资源/image-20201101162202310.png)







### 6. 部署kafka

~~~shell
#docker拉取kafka和zookeeper的镜像
docker pull wurstmeister/kafka
docker pull wurstmeister/zookeeper
#docker 打包yml参考官方文档
https://github.com/wurstmeister/kafka-docker/blob/master/docker-compose.yml
#这里我修改了kafka下的ports为9092:9092映射
~~~

~~~yml
version: '3'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka
    depends_on: [ zookeeper ]
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 10.168.1.245
      KAFKA_CREATE_TOPICS: "test:1:1"
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /data/kafka/data/docker.sock:/var/run/docker.sock
~~~

**注意！！这里kafka配置文件中填写的ip地址应为宿主机ip地址！否则docker两个容器之间无法通过localhost进行通信！**

这里提一句由于centos7的问题，docker-compose 命令不存在，需要安装一些扩展源

~~~shell
sudo yum install -y wget
#讲文件下载至/usr/local/bin并重命名，再授权
cd /usr/local/bin/
#下载
wget https://github.com/docker/compose/releases/download/1.14.0-rc2/docker-compose-Linux-x86_64
#重命名
rename docker-compose-Linux-x86_64 docker-compose docker-compose-Linux-x86_64
#授予可执行权限
chmod +x /usr/local/bin/docker-compose
~~~



~~~
#在kafka-compose.yml目录中执行命令
docker-compose build
docker-compose up -d
~~~

手动安装

~~~shell
docker run --name zk -p 2181:2181  -d wurstmeister/zookeeper


docker run --name kafka -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=10.168.1.245:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://10.168.1.245:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -v /data/kafka/data/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime   wurstmeister/kafka



~~~



### 7. 部署maven和jenkins



## 三、分布式事务框架

虽然手写底层也可以完成单点跨库的伪分布式事务操作，但这并没有什么卵用，实际集群中不可能一个transaction控制多个连接操作分布在不同主机上的mysql。

参考文档：

https://seata.io/zh-cn/docs/overview/what-is-seata.html

经过学习，这里选用阿里开源的Seata框架， Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。 

## 四、参考

#### 1. 虚拟机桥接网卡的问题，找不到桥接网卡解决方案：

WLAN属性中点击安装，如图所示操作，最后找到virtualbox安装目录下\drivers\network\netlwf的VBoxNetLwf.inf文件，重启virtualbox即可。

当然，此操作也可以在网络适配器中设定

![image-20201101124445275](./资源/image-20201101124445275.png)

![image-20201101124149612](./资源/image-20201101124149612.png)



桥接成功后，虚拟机就会进入我们的局域网中，当然也可以给它们设置固定的IP地址方便我们的服务调用

#### 2. 为虚拟机设置固定IP

一个很mmp的事情，如果你不设置，那么每次你用虚拟机的时候IP都是不一样的，那程序配置里的ip就失效了，所以还玩儿毛？因此，你可以自行设置虚拟机的固定ip，这里需要你会使用wifi的固定ip设置，因为你家联网的工具如果多的话，有小概率ip冲突的情况，因此，你可能需要考虑将一些敏感IP固定起来，当然一般家里自己玩不需要设定wifi的固定ip。

言归正传，

**前提是你的虚拟机已经设定为桥接网卡并成功安装好了centos**

打开你的虚拟机输入以下指令，打开网络配置：

~~~
vi /etc/sysconfig/network-scripts/ifcfg-enp0s3
~~~

发现原始配置如下，现在我们要退到windows，看看宿主机的配置

![image-20201101155937191](./资源/image-20201101155937191.png)

退到桌面上win+R打开cmd 输入ipconfig /all查询自己宿主机的信息，然后对应修改虚拟机中的IPADDR，NETMASK，GATEWAY即可

![image-20201101155336386](./资源/image-20201101155336386.png)

注意这些是需要设定的，修改好的配置如下

~~~shell
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static  #此处设置为static，使用指定的ip
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=enp0s3
UUID=b65134a3-13e1-4357-9d07-10bc3fdfe85f
DEVICE=enp0s3
ONBOOT=yes #此处设置为yes
IPADDR=192.168.2.100 #此处增加IPADDR为你指定的ip，192.168.x.x网段应与你的主机一致
NETMASK=255.255.255.0 #此处增加NETMASK指定的掩码
GATEWAY=192.168.2.1 #此处增加GATEWAY指定的网关
DNS1=192.168.2.1 #此处增加DNS1，与ipconfig中的DNS配置相同（默认与网关一致）

~~~



设定完成后，service network restart重启服务或重启虚拟机均可，然后使用命令

~~~
ip addr
~~~

查看ip已经修改成功，可以尝试用 ping www.baidu.com来测试外网连通性

如果ping通，那么固定ip设定成功

#### 3. 容器之间的通信问题

在环境配置的时候，安装kafka的时候，mmp的事情发生了，kafka并没有如我们所愿的注册到zookeeper上，试想如果可以的话，平时如果不用docker其实是可以上去的。问题大抵是出在了容器和容器间互相通信的时候。百度找了一些方法无果。



引入顶级父工程依赖

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.4.6</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.12</version>
</dependency>
<dependency>
    <groupId>commons-lang</groupId>
    <artifactId>commons-lang</artifactId>
    <version>2.6</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.8.0</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.8.0</version>
</dependency>
~~~

建立相应的模块，此处不表

