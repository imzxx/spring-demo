# ArrayList源码解析

## ArrayList结构

![ArrayList结构](D:\workpass\spring-demo\notes\images\ArrayList结构.png)

```java
// 继承的类和实现的接口
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
```

> 1. 继承抽象类AbstractList。这个类提供了List集合的框架实现。
> 2. 实现RandomAccess接口。集合实现使用的标记接口。
> 3. 一个类实现了Cloneable接口
>    向{@link java.lang. object# clone()}方法指明它是
>    该方法可以对该类的实例进行字段对字段的复制。

##ArrayList常量

```java
private static final long serialVersionUID = 8683452581122892189L;

    /**
     * Default initial capacity.
     * 默认初始容量。
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Shared empty array instance used for empty instances.
     * 用于空实例的共享的空数组实例。
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * Shared empty array instance used for default sized empty instances. We
     * distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     * 用于默认大小的空实例的共享空数组实例。我们将其与EMPTY_ELEMENTDATA区分开来，以便知道何时膨胀      * 需要添加第一个元素。
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     * 存储数组元素的数组缓冲区。ArrayList的容量就是这个数组缓冲区的长度。任何带有	                  * elementData==DEFAULTCAPACITY_EMPTY_ELEMENTDATA的空数组将在添加第一个元素时扩展为              * DEFAULT_CAPACITY。
     */
    transient Object[] elementData; // non-private to simplify nested class access 非私有的简化内部类访问。

    /**
     * The size of the ArrayList (the number of elements it contains).
     * 集合的大小（它包含元素的数量）
     * @serial
     */
    private int size;
```

> ArrayList中使用的常量。
>
> transient：被修饰的变量不参与序列化和反序列化。

##构造函数

```java
	/**
     * Constructs an empty list with the specified initial capacity.
     * 使用指定的初始容量构造一个空集合。
     * @param  initialCapacity  the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}
```

> 如果传入的参数initialCapacity>0，则创建一个新的Object数组放到数组缓存区中。
>
> 如果initialCapacity==0，将默认的空的Object数组放到数组缓存区。
>
> 否则，抛出IllegalArgumentException异常。