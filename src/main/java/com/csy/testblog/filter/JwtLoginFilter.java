package com.csy.testblog.filter;

import com.csy.testblog.bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    /**
     *
     * @param req :包含了用户名和密码
     * @param resp
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException, IOException, ServletException {
        User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
        //自行校验，成功则调用successfulAuthentication失败则调用successfulAuthentication
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }
    //登陆成功
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        //1.当前用户的角色，相连，2.生成token并把token返回客户端
        StringBuffer as = new StringBuffer();
        for (GrantedAuthority authority : authorities) {
            as.append(authority.getAuthority()).append(",");
            String jwt = Jwts.builder()
                    .claim("authorities", as)//配置用户角色
                    .setSubject(authResult.getName())//主题
                    .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))//过期时间
                    .signWith(SignatureAlgorithm.HS512,"sang@123")//加密
                    .compact();
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            out.write(new ObjectMapper().writeValueAsString(jwt));
            out.flush();
            out.close();
        }
    }

    //登陆失败
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write("登录失败!");
        out.flush();
        out.close();
    }
}
