package com.yql;

import com.yql.common.utils.JwtUtil;
import com.yql.sys.entity.User;
import com.yql.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class XadminApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void testMapper() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }


    @Test
    void testJwt(){
        User user = new User();
        user.setUsername("zhangsan");
        user.setPhone("12346898855");
        String token = JwtUtil.createToken(user);
        System.out.println(token);
    }
    @Test
    void testParse(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhZjgzYzJjOC1hMTYzLTQ5NWItOGZmNC0yMWUzYmZlOGVkZWYiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzNDY4OTg4NTVcIixcInVzZXJuYW1lXCI6XCJ6aGFuZ3NhblwifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTcyMzg3ODQyMiwiZXhwIjoxNzIzODgwMjIyfQ.FOLR3AgKOZEjmv0RO-GD6GagW0PGXWj7wLnxr7t1l7A";
        User user = JwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }
}
