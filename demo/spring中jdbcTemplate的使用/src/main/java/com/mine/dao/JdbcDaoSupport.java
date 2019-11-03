package com.mine.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 用于处理多个dao中的重复代码.
 * 当前模块本身在spring中已经实现过, 当JdbcDaoSupport继承spring的时候,
 * 不需要我们在自定义这个类来处理重复代码的情况.
 * 但是由于jdbcTemplate在sping的jar包内部,所以如果是基于纯注解开发,则无法注入jdbcTemplate.
 * 所以在纯注解开发中,我们一般采用当前文件中的方式自定义JdbcDaoSupport来抽取代码的重复部分
 * 当我们使用xml配置时, 可以直接继承spring的JdbcDaoSupport即可,不需要在提供JdbcDaoSupport类来处理.
 */
public class JdbcDaoSupport {
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        if (jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate(dataSource);
        }
    }

    private JdbcTemplate createJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
