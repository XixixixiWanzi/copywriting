package com.example.demo.UserDetailService;

import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // 查询用户信息
        Account account = mapper.selectById(id);

        if (account == null) {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }

//        todo: 权限管理 , 现在默认 都是 admin
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
//        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("admin"));


        return new org.springframework.security.core.userdetails.User(id, account.getSecret(), authorities);
    }
}
