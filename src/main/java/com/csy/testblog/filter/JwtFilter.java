package com.csy.testblog.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class JwtFilter extends GenericFilterBean {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        //1.得到客户端传来的token
        String jwtToken = req.getHeader("authorization");
        System.out.println(jwtToken);
        //2token字符串转换为一个Claims对象
        Claims claims = Jwts.parser().setSigningKey("sang@123").parseClaimsJws(jwtToken.replace("Bearer","")).getBody();
        //3.获取当前登录用户名
        String username = claims.getSubject();
        //4获得当前用户的角色
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
        //5.创建一个UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
        //6.Context
        SecurityContextHolder.getContext().setAuthentication(token);
        //7执行过滤链
        filterChain.doFilter(req,servletResponse);
    }

}
