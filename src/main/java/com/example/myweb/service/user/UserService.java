package com.example.myweb.service.user;

import com.example.myweb.repository.UserRepository;
import com.example.myweb.request.UserPrincipal;
import com.example.myweb.table.Role;
import com.example.myweb.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        String newPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(newPass);
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, "ROLE_USER"));
        user.setRoles(roles);
        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User findByUsername(String name) {
        return repository.findByUsername(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        return UserPrincipal.build(user);
    }
}
