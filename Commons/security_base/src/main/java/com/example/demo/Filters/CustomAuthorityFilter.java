package com.example.demo.Filters;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Supplier;

@Component
public class CustomAuthorityFilter {
    public AuthorizationDecision authorizationDecision(Supplier<Authentication> authentication, RequestAuthorizationContext object){
        boolean isMatch = true;
        String requestURI = object.getRequest().getRequestURI();


        //todo 动态权限认证过程
        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN")) {
                //说明当前登录用户具备当前请求所需要的角色
                return new AuthorizationDecision(true);
            }
        }

        if (!isMatch) {
            //说明请求的 URL 地址和数据库的地址没有匹配上，对于这种请求，统一只要登录就能访问
            if (authentication.get() instanceof AnonymousAuthenticationToken) {
                return new AuthorizationDecision(false);
            } else {
                return new AuthorizationDecision(true);
            }
        }
        return new AuthorizationDecision(false);
    }
}
