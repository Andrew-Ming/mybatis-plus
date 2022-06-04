package com.example.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.mybatisplus.mapper.UserMapper;
import com.example.mybatisplus.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class WrapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void test01() {
        // 查询用户名包含 a ，年龄在20到30之间，并且邮箱不为 null 的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", "a")
                .between("age", 20, 30)
                .isNotNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void test02() {
        // 按年龄降序查询用户，如果年龄相同则按ID升序排列
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age")
                .orderByAsc("id");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void test03() {
        // 删除email为空的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int result = userMapper.delete(queryWrapper);
        log.info("result:{}", result);
    }

    @Test
    void test04() {
        // 将（年龄大于20并且用户名包含有a）或邮箱为 null 的用户信息修改
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", "a")
                .gt("age", 20)
                .or()
                .isNull("email");

        User user = new User();
        user.setAge(18);
        user.setEmail("12@.com");
        int result = userMapper.update(user, queryWrapper);
        log.info("result:{}", result);
    }

    @Test
    void test05() {
        // 将用户名包含有 a 并且（年龄大于20或邮箱为 null ）的用户信息修改
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", "a")
                .and(i -> i.gt("age", 20).or().isNull("email"));

        User user = new User();
        user.setAge(18);
        user.setEmail("122@.com");
        int result = userMapper.update(user, queryWrapper);
        log.info("result:{}", result);
    }

    @Test
    void test06() {
        // 查询用户信息的 username 和 age
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("username", "age");

        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }

    @Test
    void test07() {
        // 查询 ID 小于等于 3 的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id", "select id from user where id <= 3");
        List<User> uses = userMapper.selectList(queryWrapper);
        uses.forEach(System.out::println);
    }

    @Test
    void test08() {
        // 将（年龄大于20或邮箱为 null )并且用户名中包含有 a 的用户信息修改
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        // lambda 表达式内的逻辑优先运算
        updateWrapper.set("age", 18)
                .set("email", "12")
                .like("username", "a")
                .and(i -> i.gt("age", 20).or().isNull("email"));

        int result = userMapper.update(null, updateWrapper);
        log.info("result:{}", result);
    }

    /**
     * 构建查询条件
     */
    @Test
    void test09() {
        String username = null;
        Integer ageBegin = 10;
        Integer ageEnd = 24;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", "a")
                .ge(ageBegin != null, "age", ageBegin)
                .le(ageEnd != null, "age", ageEnd);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    /**
     * lambda 查询
     */
    @Test
    void test10() {
        String username = null;
        Integer ageBegin = 10;
        Integer ageEnd = 24;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), User::getName, username)
                .ge(ageBegin != null, User::getAge, ageBegin)
                .le(ageEnd != null, User::getAge, ageEnd);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * lambda 修改
     */
    @Test
    void test11() {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.set(User::getAge, 18)
                .set(User::getEmail, "12.@com")
                .like(User::getName, "a")
                .and(i -> i.lt(User::getAge, 24).or().isNull(User::getEmail));
        User user = new User();
        int result = userMapper.update(user, updateWrapper);
        log.info("result:{}", result);
    }



}
