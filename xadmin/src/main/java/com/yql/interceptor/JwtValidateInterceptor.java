package com.yql.interceptor;

import com.alibaba.fastjson2.JSON;
import com.yql.common.utils.JwtUtil;
import com.yql.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author yql
 * @Date 2024/8/17 15:51
 * @Description:
 */

@Slf4j
@Component
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("x-token");
        log.debug(request.getRequestURI() + "需要验证" + token);
        if (null != token){
            try {
                JwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证成功");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败");
        response.setContentType("application/json;charset=utf-8");
        Result<Object> fail = Result.fail(50008, "jwt令牌无效，请重新登录");
        String jsonString = JSON.toJSONString(fail);
        response.getWriter().print(jsonString);
        return false;
    }
}

