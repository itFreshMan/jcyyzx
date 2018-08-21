package com.tydic.core.plugin.mybatis;

import com.tydic.core.util.Strings;
import com.tydic.core.util.collection.Lists;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 动态sql
 *
 * @author jiangshijian
 * @author Hao Peng
 * @author Guang YANG
 */
public final class SQLMapper {

  private static final Map<String, String> cache = new ConcurrentHashMap<>();

  // Suppress default constructor for non-instant ability.
  private SQLMapper() {
    throw new AssertionError();
  }

  /**
   * 新增MappedStatement至Mybatis Configuration内
   *
   * @param configuration 从sqlSession中获取configuration
   * @param id SQL ID
   * @param sql sql体
   * @param type QUERY类型、详见{@link SqlCommandType}
   * @param clazz 返回类型，HashMap.class、LinkedHashMap.class
   */
  public static synchronized void addMappedStatement(final Configuration configuration, String id, String sql,
      SqlCommandType type, Class<?> clazz) {
    Lists.of(configuration, id, sql, type, clazz).forEach(Objects::requireNonNull);
    if (cache.containsKey(id) && sql.equals(cache.get(id))) {
      return;
    }
    synchronized (Configuration.class) {
      if (Map.class.isAssignableFrom(clazz)) {
        addMappedStatement(configuration, id, sql, type, clazz, Lists.of());
      } else {
        addMappedStatement(configuration, id, sql, type, clazz, makeResultMapping(configuration, clazz));
      }
      cache.put(id, sql);
    }
  }

  /**
   * 新增MappedStatement至Mybatis Configuration内
   *
   * @param configuration 从sqlSession中获取configuration
   * @param id SQL ID
   * @param sql sql体
   * @param type QUERY类型、详见{@link SqlCommandType}
   * @param clazz 返回类型，如果返回类型为实体类，则需要定义resultMapping，可以使用makeResultMapping方法
   */
  private static void addMappedStatement(final Configuration configuration, String id, String sql,
      SqlCommandType type, Class<?> clazz,
      List<ResultMapping> resultMappings) {
    while (configuration.hasStatement(id)) {
      removeStatement(configuration, id);
    }

    sql = "<script>" + sql + "</script>";
    LanguageDriver languageDriver = configuration.getDefaultScriptingLanguageInstance();
    SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, clazz);
    MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id,
        sqlSource, type);

    statementBuilder.resultMaps(Lists.of(
        new ResultMap.Builder(configuration, "defaultResultMap", clazz, resultMappings).build()
    ));

    MappedStatement statement = statementBuilder.build();
    while (!configuration.hasStatement(id)) {
      try {
        configuration.addMappedStatement(statement);
      } catch (Exception ignored) {
      }
    }

  }

  private static void removeStatement(final Configuration configuration, String id) {
    configuration.getMappedStatements().remove(configuration.getMappedStatement(id));
  }

  /**
   * 映射实体成员变量
   *
   * @param configuration 从sqlSession中获取configuration
   * @param clazz 返回类型
   * @return 实体成员变量
   */
  private static List<ResultMapping> makeResultMapping(final Configuration configuration, Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .filter(f -> !Modifier.isStatic(f.getModifiers()))
        .map(f -> build(configuration, f))
        .collect(Collectors.toList());
  }

  private static ResultMapping build(final Configuration configuration, Field field) {
    return new ResultMapping.Builder(
        configuration,
        Strings.camelName(field.getName()),
        field.getName().toUpperCase(),
        field.getType()).build();
  }

}
