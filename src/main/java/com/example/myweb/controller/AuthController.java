package com.example.myweb.controller;

import com.example.myweb.config.JwtProvider;
import com.example.myweb.request.LoginRequest;
import com.example.myweb.request.RegisterRequest;
import com.example.myweb.response.JwtResponse;
import com.example.myweb.response.Resp;
import com.example.myweb.service.user.IUserService;
import com.example.myweb.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtProvider jwtService;

    @PostMapping("/login")
    public ResponseEntity<Resp> login(@RequestBody LoginRequest request) {
        Resp resp = new Resp();
     try {
         //Kiểm tra username và pass có đúng hay không
         Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
         //Lưu user đang đăng nhập vào trong context của security
         SecurityContextHolder.getContext().setAuthentication(authentication);

         String jwt = jwtService.generateTokenLogin(authentication);
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         User currentUser = userService.findByUsername(request.getUsername());
           resp.setData(new JwtResponse(currentUser.getId(), jwt,"Bearer", userDetails.getUsername() , userDetails.getAuthorities()));
           resp.setMsg( "Thành công");
     } catch (Exception e) {
         resp.setMsg(e.getMessage());
     }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<Resp> register( @RequestBody RegisterRequest request)  {
        Resp resp = new Resp();
        try{
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                    throw new Exception("Mật khẩu không trùng khớp");
            }
            for (User user : userService.findAll() ) {
                if (user.getUsername().equals(request.getUsername())) {
                    throw new Exception("Tên tài khoản đã tồn tại");
                }
            }

            User user1 = new User(request.getUsername(), request.getPassword());
            userService.save(user1);
            resp.setData(user1);
            resp.setMsg("Thành công");
        } catch (Exception e) {
            resp.setMsg(e.getMessage());
        }
        return ResponseEntity.ok(resp);
    }
}
