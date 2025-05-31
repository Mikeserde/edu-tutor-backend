package com.example.server.controller;

import com.example.server.model.User;
import com.example.server.utils.JwtUtils;
import com.example.server.utils.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        String token = JwtUtils.generateToken(user.getUsername());
        return Result.ok().data("token",token);
    }

    @GetMapping("/info")
    public Result info(String token){
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        String url = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
        return Result.ok()
                .data("roles","admin")
                .data("introduction","I am a super administrator")
                .data("avatar",url)
                .data("name",username);
    }

    @PostMapping("/logout")
    public Result logout(){return Result.ok();}
}
