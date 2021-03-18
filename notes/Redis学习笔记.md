# Redis学习笔记

## Redis是什么

> Redis：REmote DIctionary Server(远程字典服务器)
>
> 是完全开源免费的，用c语言编写的，遵循BSD协议。
>
> 是一个高性能的分布式内存数据库，基于内存运行并支持持久化的NoSQL数据库，是当前最热门的NoSQL数据库之一，也被称为数据结构服务器。

## Redis的三个特点

> 1. 支持数据的持久化，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用。
> 2. 不仅仅支持简单的key-value类型的数据，同时还提供list，set，zset，hash等数据结构的存储。
> 3. 支持数据的备份，即master-slave模式的数据备份。

## Redis五大数据类型

> 1. String：字符串
>
>    string类型是二进制安全的。就是Redis的String可以包含任何数据。比如jpg图形或者序列化对象。
>
>    String类型是Redis最基本的数据类型，一个Redis中的字符串value最多可以是512M。
>
> 2. Hash：哈希
>
>    Redis hash是一个键值对集合。是String类型的field和value的映射表，hash特别适合用于存储对象。
>
> 3. List：列表
>
>    Redis列表是简单的字符串列表，按照插入顺序排序。可以添加一个元素到列表的头部或者尾部。底层实际是个链表。
>
> 4. Set：集合
>
>    Redis的set是String类型的无序集合，是通过HashTable实现的。
>
> 5. Zset：有序集合
>
>    相当于在set类型的数据中加了一个double类型的分数。

## Redis键（key）常用命令

### keys *

> 查询数据库下所有的key。

### exists

> exists [key的名字]，判断某个key是否存在。

### move

> move [key db] ，将当前数据库中的数据移到指定数据库。

### expire

> expire [key 秒]，为给定的key设置过期时间。

### ttl

> ttl [key]，查看还有多少秒过期，-1表示永不过期，-2表示已过期。

### type

> type [key],查看key是什么类型。

## Redis字符串（String）常用命令

### set/get/del/append/strlen

> del [key]，从内存中将数据删除。
>
> set [key value]，这是key和value值。
>
> get[key]，从内存中取出key的value值。
>
> append [key str]，将key的value值的后面加上str字符串。
>
> strlen [key]，查询value值的字符长度。

### incr/decr/incrby/decrby

> 对value的值进行加减，value的值必须是数字。
>
> incr [key]，对key的值进行加一。
>
> decr [key]，对key的值进行减一。
>
> incrby [key number]，对key的值进行加number。
>
> decrby [key number]，对key的值进行减number。

### getrange/setrange

> getrange [key index end]，获取key的值，从index截取到end结束。如果截取的结束值大于value值的长度，则从index取到value值的最后一个。如果index的值大于value值的长度，则获取一个空的字符。
>
> setrange [key offset value]，获取key的值，从offset开始将原来的值替换成value。如果设置的值超出原有值的长度，则扩展原有的值的长度至最新值。

### setex(set with expire)键秒值/setnx(set if not exist)

> setex [key seconds value]，设置key value键值对，生命周期为seconds秒。
>
> setnx [key value]，设置key value键值对，如果数据库中已存在，不会覆盖之前的数据，则命令不生效。

### mset/mget/msetnx

> mset [key1 value1 key2 value2 ...]，对多个值进行set。
>
> mget [key1key2 ...]，对多个值进行get。
>
> msetnx [key1 value1 key2 value2 ...]，对多个值进行setnx。当设置的值已经存在时，整个命令都不会生效。

## Redis列表（List）常用命令

### lpush/rpush/lrange

> lpush [key value...]，键为key，值为列表value...。l表示左，最终查询出来的结果 由左往右显示。
>
> rpush [key value...]，键为key，值为列表value...。r表示右，最终查询出来的结果由右网左显示。
>
> lrange [key start end]，获取键为key的value值，值从start开始end结束。end=-1时，查询到值的最后一位。

### lpop/rpop

> lpop [key]，弹出列表最左边的值，弹出以后列表将不存在该值。
>
> rpop [key]，弹出列表最右边的值，弹出以后列表将不存在该值。

### lindex

> lindex [key index]，按照索引下标获取元素(从上到下获取)

### llen

> llen [key]，获取列表的长度。

### lrem

> lrem [key n value]，删除列表中n个value的值。 

### ltrim

> ltrim [key start end]，截取start到end范围的值，再赋值给value。

### rpoplpush

> rpoplpush [key1 key2]，将源列表最右边的值压入到目标列表最左边。

### lset

> lset [key index value]，将key的下标为index的值替换成value。

### linsert 

> linsert [key before/after value1 value2]，将键为key的列表中值为value1之前或之后加上value2。

## Redis集合（Set）常用命令

### sadd/smembers/sismember

> sadd [key value...]，value的值不能重复，如果有重复不会报错，数据库中只会存储一个。
>
> smembers [key]，查询键为key下的所有集合。
>
> sismember [key value]，查询集合中是否存在value值。

### scard

> scard [key]，获取集合中有多少元素。

### srem 

> srem [key value...]，删除集合中的value 元素。

### srandmember

> srandmember [key number]，获取集合中随机number个值。

### spop

> spop [key]，随机出栈。

### smove

> smove [key1 key2 value]，将集合1中的value移到集合2中。

### sdiff/sinter/sunion

> sdiff [key1 key2]，差集。
>
> sinter [key1 key2]，交集。
>
> sunion [key1 key2]，并集。

## Redis哈希（Hash）常用命令

> kv模式不变，但v是一个键值对。

### hset/hget/hmset/hmget/hgetall/hdel

> hset [key hkey hvalue]，value值是一个键值对形式。
>
> hget [key hkey]，获取一个键为kay，值的键为hkey。
>
> hmset [key hkey1 hvalue1 hkey2 hvalue2 ...]，多值存储。
>
> hmget [key hkey1 hkey2 ...]，多值获取。
>
> hgetall [key]，获取所有的值。
>
> hdel [key]，删除Hash。

### hlen

> hlen [key]，查询Hash值的个数。

### hexists

> hexists [key hkey1]，判断哈希是否存在。

### hkeys/hvals

> hkeys [key]，遍历所有的key。
>
> hvals [key]，遍历所有的value。

### hincrby/hincrbyfloat

> hincrby [key hkey1 number]，哈希值加上2。
>
> hincrbyfloat [key hkey1 float]，哈希值加上小数。

### hsetnx

> hsetnx [key hkey1 hvalue1]，当值不存在时才去插入。

## Redis有序集合（Zset）的常用命令

> zset是在set的基础上加一个score值。形如：key1 score1 v1 score2 v2 ...

### zadd/zrange

> zadd [key score1 v1 score2 v2 ...]，添加zset的值。
>
> zrange [key start end] withscores，遍历zset中的值。withscores是遍历值和sore。

### zrangebyscore 

> zrangebyscore [key score1 （score2] limit start number，根据score获取值。 加上（ 表示不包含。limit表示在结果中再截取，从start开始获取number个。

### zrem

> zrem [key value]，删除key中的一个集合。

### zcard/zcount

> zcard [key]，统计个数。
>
> zcount [key score1 score2]，按分数统计个数。

## Redis的持久化

### rdb

> rdb：Redis DataBase。

> 就是在指定的时间间隔内将内存中的数据集快照写入磁盘，也就是行话讲的Snapshot快照，它恢复时是将快照文件直接读到内存中。
>
> Redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。
>
> 整个过程中，主进程是不进行任何IO操作的，这就确保了极高的性能。
>
> 如果需要进行大规模数据的恢复，且对于数据恢复的完成性不是非常敏感，那rdb方式要比aof方式更加的高效。
>
> rdb的缺点是最后一次持久化后的数据可能丢失。

### aof

