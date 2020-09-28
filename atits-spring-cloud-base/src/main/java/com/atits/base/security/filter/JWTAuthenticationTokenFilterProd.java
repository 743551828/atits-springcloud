package com.atits.base.security.filter;

import com.atits.base.item.Payload;
import com.atits.base.item.SelfUserDetail;
import com.atits.base.properties.JWTProperties;
import com.atits.base.utils.JwtUtils;
import com.atits.base.utils.ResultUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT接口请求校验拦截器
 * 请求接口时会进入这里验证Token是否合法和过期
 * @author: zhangys
 * @create: 2020-09-10 13:52
 */
@Slf4j
public class JWTAuthenticationTokenFilterProd extends BasicAuthenticationFilter {

    public JWTAuthenticationTokenFilterProd(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中JWT的Token
        String tokenHeader = request.getHeader(JWTProperties.tokenHeader);
        Object tokenAttr = request.getAttribute(JWTProperties.tokenHeader);
        String tokenParam = request.getParameter(JWTProperties.tokenHeader);
        String wholeToken = "";
        if (tokenHeader != null){
            wholeToken = tokenHeader;
        }else if (tokenAttr != null){
            wholeToken = (String) tokenAttr;
        }else if (tokenParam != null){
            wholeToken = tokenParam;
        }

        if (wholeToken.startsWith(JWTProperties.tokenPrefix)) {
            try {
                // 截取JWT前缀
                String token = wholeToken.replace(JWTProperties.tokenPrefix, "");
                // 解析JWT
                Payload<SelfUserDetail> payload = JwtUtils.getInfoFromToken(token, JWTProperties.publicSecret, SelfUserDetail.class);
                SelfUserDetail userInfo = payload.getUserInfo();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, userInfo.getUserId(), userInfo.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (ExpiredJwtException e){
                ResultUtil.responseJson(response,ResultUtil.resultCode(401,"Token过期"));
            } catch (Exception e) {
                ResultUtil.responseJson(response,ResultUtil.resultCode(401,"Token无效"));
            }
        }else {
            ResultUtil.responseJson(response,ResultUtil.resultCode(401,"未携带tokenToken"));
        }
    }
}