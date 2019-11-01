package cn.mine.jedis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * JedisPool工具类
 * 加载配置文件, 配置连接池参数
 * 提供获取连接的方法
 */
public class JedisPoolUtils {
    private static JedisPool jedisPool;
    static {
        // 读取配置文件
        InputStream is = JedisPoolUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        Properties pro = new Properties();

        // 获取数据,设置到jedisPoolConfig中
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(pro.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(pro.getProperty("maxIdle")));

        // 初始化jedisPool
        jedisPool = new JedisPool(config,pro.getProperty("host"), Integer.parseInt(pro.getProperty("port")));
    }

    /**
     * 获取连接方法
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}

