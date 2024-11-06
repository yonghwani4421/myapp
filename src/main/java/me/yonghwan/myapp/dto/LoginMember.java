package me.yonghwan.myapp.dto;

import lombok.Data;
import me.yonghwan.myapp.domain.Role;

@Data
public class LoginMember {
    private String email;
    private String password;
    private Role role;
}
