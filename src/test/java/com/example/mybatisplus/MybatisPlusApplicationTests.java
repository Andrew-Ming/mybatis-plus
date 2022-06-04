package com.example.mybatisplus;

import com.example.mybatisplus.mapper.UserMapper;
import com.example.mybatisplus.pojo.User;
import com.example.mybatisplus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    /**
     * 查询所有
     */
    @Test
    void selectList() {
        userMapper.selectList(null).forEach(System.out::println);
    }

    /**
     * 插入数据
     */
    @Test
    void insert() {
        User user = new User(null, "张三", 23, "10.com");
        int result = userMapper.insert(user);
        log.info("result:{}, id:{}", result, user.getId());
    }

    /**
     * 通过 ID 删除
     */
    @Test
    void deleteById() {
        int result = userMapper.deleteById(122);
        log.info("result:{}", result);
    }

    @Test
    void deleteBatchIds() {
        List<Long> idList = Arrays.asList(1L, 2L, 3L);
        int result = userMapper.deleteBatchIds(idList);
        log.info("result:{}", result);
    }

    @Test
    void deleteByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 23);
        map.put("name", "张三");
        int result = userMapper.deleteByMap(map);
        log.info("result:{}", result);
    }

    @Test
    void updateById() {
        User user = new User(4L, "admin", 22, null);
        int result = userMapper.updateById(user);
        log.info("result:{}", result);
    }

    @Test
    void selectById() {
        User user = userMapper.selectById(4L);
        log.info("user:{}", user.toString());
    }

    @Test
    void selectBatchIds() {
        List<Long> idList = Arrays.asList(4L, 5L);
        List<User> users = userMapper.selectBatchIds(idList);
        users.forEach(System.out::println);
    }

    @Test
    void selectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 22);
        map.put("name", "admin");
        List<User> userList = userMapper.selectByMap(map);
        userList.forEach(System.out::println);
    }


    @Test
    void getCount() {
        long count = userService.count();
        log.info("count:{}", count);
    }

    @Test
    void saveBatch() {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("zhu");
            user.setAge(20 + i);
            users.add(user);
        }

        userService.saveBatch(users);
    }
}
