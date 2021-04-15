# Java集合面试题

## Java中常见的集合可以概况如下

> * Map接口，Collection接口是所有集合框架的父类。
> * Collection接口的子接口包括：Set接口和List接口。
> * Set接口的实现类主要有：HashSet，TreeSet，LinkedHashSet等。
> * List接口的实现主要有：ArrayList，LinkedList，Stack，Vector，CopyOnWriteArrayList等。
> * Map的实现类主要有：HashMap，TreeMap，HashTable，LinkedHashMap，ConcurrenHashMap等。

## 什么是迭代器

> Iteratro提供了统一遍历操作集合元素的统一接口，Collection接口实现Iterable接口。
>
> 每个集合都通过实现Iterable接口中iterator()方法返回Iterator接口的实例，然后对集合的元素进行迭代。

## ArrayList和LinkedList有哪些区别

> * ArrayList底层使用了动态数组实现。
> * LinkedList底层使用了双向链表实现，可当做堆栈，队列，双端队列使用。
> * ArrayList在随机存取方面效率高于LinkedList。
> * LinkedList在节点的增删方面效率高于ArrayList。
> * ArrayList必须预留一定的空间，当空间不足时，会进行扩容。
> * LinkedList的开销是必须存储节点的信息以及节点的指针信息。

## HashSet和TreeSet有哪些区别

> * HashSet底层使用了Hash表实现，保证元素唯一性的原理：判断元素的hashCode值是否相同，如果相同，还会继续判断元素的equals方法，是否为true。
> * TreeSet底层使用了红黑树来实现，保证元素唯一性是通过Comparable或者Comparator接口实现。

## HashMap底层实现原理

> HashMap底层实现数据结构为数组+链表的形式，JDK8及其以后的版本中使用了数组+链表+红黑树实现。

## HashMap的初始容量，加载因子，扩容增量是多少

> Hash的初始容量16，加载因子为0.75，扩容增量是原容量的1倍。

## HashMap和Hashtable的异同

> 1. 对null key和null value的支持不同。HashTable不允许null值，HashMap允许使用null值。这样的键只有一个，可以有一个或多个键对应的值为null。
> 2. 同步性。HashTable的方法时同步的，HashMap不能同步。因此HashMap更适合于单线程环境，而HashTable更适合于多线程环境。
> 3. 继承的父类不同。HashTable继承自Dictionary类，而HashMap是继承自AbstractMap类。
> 4. 遍历方法不同。HashTable使用Enumeration遍历，HashMap使用Iterator进行遍历。

## ConcurrentHashMap的原理

> ConcurrentHashMap类中包含两个静态内部类HashEntry和Segment。HashEntry用来封装映射表的键/值对；Segment用来充当锁的角色，每个Segment对象守护整个散列映射表的若干个桶。
>
> 在ConcurrentHashMap中，在散列是如果产生碰撞，将采用分离链接法来处理碰撞：把碰撞的HashEntry对象链接成一个链表。由于HashEntry的next域为final型，所以新节点只能在链表的表头处插入。
>
> Segment继承ReentrantLock类，从而使得Segment对象能充当锁的角色。

## ConcurrentHashMap有什么优势以及1.7和1.8的区别

> ConcurrentHashMap是线程安全的，在1.7是采用Segment+HashEntry的方式实现的，lock加在Segment上。1.7的size计算是先采用不加锁的方式，连续计算元素的个数，最多计算3次。1.8中放弃了Segment臃肿的设计，取而代之的代用了Node+CAS+synchronized来保证并发安全进行实现，1.8中使用一个volatile类型的变量baseCount记录元素的个数，当插入或删除数据时，会通过addCount()方法更新baseCount，通过累加baseCount和CounterCell数组中的数量，即可得到元素的总个数。

> jdk1.7：Segment，段的概念，一个段包含多个HashEntry，实行分段加锁。
>
> jdk1.8：put操作的锁粒度更细化，并且扩容的时候效率更高。
>
> * 去掉Segment这种数据结构，只用一个元素类型为HashEntry的数组，只对元素HashEntry（首节点）加锁。
> * 使用内置锁synchronized来代替重入锁ReentrantLock（synchronized会根据锁的争用情况进行膨胀，相比ReentrantLock性能会提高不少，并且基于API的ReentrantLock会开销更多的内存）