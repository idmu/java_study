package cn.mine.jedis.test;

import cn.mine.jedis.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;

public class JedisTest {
    public void test() {
        Jedis jedis = JedisPoolUtils.getJedis();
        jedis.set("hello","world");
        jedis.close();
    }
}
