# Spring Cache

## 参考文档
- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-caching)
- [Spring Framework](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#cache)

## 用法和避坑指南
### 四大对象，都可以自定义
- CacheManager
- CacheResolver
- KeyGenerator
- CacheErrorHandler

- 缓存方法中的参数如果是对象，最好需要实现 `hashCode()` 和 `equals()`，因为会根据这个生成缓存 key
- `KeyGenerator` 是可以自定义的，但大部分情况下不需要，因为Spring EL很强大了
- 缓存 key 要配置重点，有些方法的参数是查询过滤条件，如果加进去，对缓存命中有影响，比如：
```java
@Cacheable("books")
public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)
```
- `CacheResolver` 也是可以自定义的，暂时没碰到过 case
- 一个应用中可以有多个 `cacheManager`， 注解中也是可以指定的
- @CacheEvict 时，为防止出错，导致清缓存失败，应加上 beforeInvocation = true
```
@CacheEvict(key = "#id", beforeInvocation = true)
```
- 多线程环境中，可以指定 `sync=true`
- `@CachePut` 和 `@Cacheable` 不要同时使用
- Spring Cache 是使用 AOP 实现的，需要 aop 相关的依赖
- 正因为使用了AOP, 以下的 case，要能缓存不生效
```
外部调用了一个类 Class1 里的 method1 (无缓存), method1 又调用了本类里的 method2 (有缓存)
invoker -> Class1.method1 -> Class1.method2
这时缓存不生效，解决的办法是将 2 个方法重构到不同的类里
实际项目中最好建立一个 cache deledate 类和处理缓存
```
- 用不同 key 缓存的同一对象不能共享，这个要特别注意脏数据的问题。比如用 id 和 name 作为 key 同时缓存了 User 对象
