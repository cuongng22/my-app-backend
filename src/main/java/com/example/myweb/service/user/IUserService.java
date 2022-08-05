package com.example.myweb.service.user;

import com.example.myweb.service.BaseService;
import com.example.myweb.table.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends BaseService<User>, UserDetailsService {
    User findByUsername(String name);
}
