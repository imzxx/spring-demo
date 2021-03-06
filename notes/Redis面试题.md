# Redis面试题

## Redis数据类型

> 传统五种数据类型：string,list,hash,set,zset。
>
> 新增数据类型：bitmap,HyperLogLog,GEO,Stream。



## Redis哨兵模式

> 哨兵模式是一种特殊的模式，首先Redis提供了哨兵的命令，哨兵是一种独立的进程，作为进程，会独立运行。其原理是哨兵通过发送命令，等待Redis服务器响应，从而监控运行的多个Redis实例。

### 作用

> * 通过发送命令让Redis服务器返回监控其运行状态，包括主服务器和从服务器。
>
> * 当哨兵检测到master宕机，会自动将slave切换到master，然后通过发布订阅模式通知其他的服务器，修改配置文件，让他们切换主机。
>
>   然而一个哨兵进程对Redis服务器进行监控，可能会出现问题，为此可以使用多个哨兵进行监控。各个哨兵之间还会进行监控，形成多哨兵模式。

