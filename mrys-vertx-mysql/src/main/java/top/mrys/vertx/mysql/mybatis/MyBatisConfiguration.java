package top.mrys.vertx.mysql.mybatis;

import static org.apache.ibatis.session.AutoMappingBehavior.PARTIAL;
import static org.apache.ibatis.session.AutoMappingUnknownColumnBehavior.NONE;
import static org.apache.ibatis.session.ExecutorType.SIMPLE;
import static org.apache.ibatis.session.LocalCacheScope.SESSION;
import static org.apache.ibatis.type.JdbcType.OTHER;

import io.vertx.core.impl.ConcurrentHashSet;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.ibatis.executor.loader.javassist.JavassistProxyFactory;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.EnumTypeHandler;

/**
 * @author mrys
 * @date 2020/8/5
 */
public class MyBatisConfiguration extends Configuration {

  private final ConcurrentHashSet<EnumType> enumTypes=new ConcurrentHashSet<>();


  {
    //全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。
    cacheEnabled = true;
    //延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 fetchType 属性来覆盖该项的开关状态。
    lazyLoadingEnabled = false;
    //开启时，任一方法的调用都会加载该对象的所有延迟加载属性。 否则，每个延迟加载属性会按需加载（参考 lazyLoadTriggerMethods)。
    aggressiveLazyLoading = false;
    //是否允许单个语句返回多结果集（需要数据库驱动支持）。
    multipleResultSetsEnabled = true;
    //使用列标签代替列名。实际表现依赖于数据库驱动，具体可参考数据库驱动的相关文档，或通过对比测试来观察。
    useColumnLabel = true;
    //允许 JDBC 支持自动生成主键，需要数据库驱动支持。如果设置为 true，将强制使用自动生成主键。尽管一些数据库驱动不支持此特性，但仍可正常工作（如 Derby）。
    useGeneratedKeys = false;
    //指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示关闭自动映射；PARTIAL 只会自动映射没有定义嵌套结果映射的字段。 FULL 会自动映射任何复杂的结果集（无论是否嵌套）。
    autoMappingBehavior = PARTIAL;
    //指定发现自动映射目标未知列（或未知属性类型）的行为。
    //NONE: 不做任何反应
    //WARNING: 输出警告日志（'org.apache.ibatis.session.AutoMappingUnknownColumnBehavior' 的日志等级必须设置为 WARN）
    //FAILING: 映射失败 (抛出 SqlSessionException)
    autoMappingUnknownColumnBehavior = NONE;
    //配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（PreparedStatement）； BATCH 执行器不仅重用语句还会执行批量更新。
    defaultExecutorType = SIMPLE;
    //设置超时时间，它决定数据库驱动等待数据库响应的秒数。
    defaultStatementTimeout = null;
    //为驱动的结果集获取数量（fetchSize）设置一个建议值。此参数只可以在查询设置中被覆盖。
    defaultFetchSize = null;
    //指定语句默认的滚动策略。（新增于 3.5.2）
    defaultResultSetType = null;
    //是否允许在嵌套语句中使用分页（RowBounds）。如果允许使用则设置为 false
    safeRowBoundsEnabled = false;
    //是否允许在嵌套语句中使用结果处理器（ResultHandler）。如果允许使用则设置为 false。
    safeResultHandlerEnabled = true;
    //是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。
    mapUnderscoreToCamelCase = true;
    //MyBatis 利用本地缓存机制（Local Cache）防止循环引用和加速重复的嵌套查询。 默认值为 SESSION，会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地缓存将仅用于执行语句，对相同 SqlSession 的不同查询将不会进行缓存。
    localCacheScope = SESSION;
    //当没有为参数指定特定的 JDBC 类型时，空值的默认 JDBC 类型。 某些数据库驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，比如 NULL、VARCHAR 或 OTHER。
    jdbcTypeForNull = OTHER;
    //指定对象的哪些方法触发一次延迟加载。
    lazyLoadTriggerMethods = new HashSet<>(
        Arrays.asList("equals", "clone", "hashCode", "toString"));
    //指定动态 SQL 生成使用的默认脚本语言。
    setDefaultScriptingLanguage(XMLLanguageDriver.class);
    //指定 Enum 使用的默认 TypeHandler
    setDefaultEnumTypeHandler(EnumTypeHandler.class);
    //指定当结果集中值为 null 的时候是否调用映射对象的 setter（map 对象时为 put）方法，这在依赖于 Map.keySet() 或 null 值进行初始化时比较有用。注意基本类型（int、boolean 等）是不能设置成 null 的。
    callSettersOnNulls = false;
    //当返回行的所有列都是空时，MyBatis默认返回 null。 当开启这个设置时，MyBatis会返回一个空实例。 请注意，它也适用于嵌套的结果集（如集合或关联）。（新增于 3.4.2）
    returnInstanceForEmptyRow = false;
    //指定 MyBatis 增加到日志名称的前缀。
    logPrefix = null;
    //指定 MyBatis 所用日志的具体实现，未指定时将自动查找。
    logImpl = null;
    //指定 Mybatis 创建可延迟加载对象所用到的代理工具。
    proxyFactory = new JavassistProxyFactory();
    //指定 VFS 的实现
    vfsImpl = null;
    //允许使用方法签名中的名称作为语句参数名称。 为了使用该特性，你的项目必须采用 Java 8 编译，并且加上 -parameters 选项。（新增于 3.4.1）
    useActualParamName = true;
    //指定一个提供 Configuration 实例的类。 这个被返回的 Configuration 实例用来加载被反序列化对象的延迟加载属性值。 这个类必须包含一个签名为static Configuration getConfiguration() 的方法。（新增于 3.2.3）
    configurationFactory = null;
    //Removes extra whitespace characters from the SQL. Note that this also affects literal strings in SQL. (Since 3.5.5)
    shrinkWhitespacesInSql = false;
  }

  public ConcurrentHashSet<EnumType> getEnumTypes() {
    return enumTypes;
  }
}
