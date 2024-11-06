package me.yonghwan.myapp.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class CustomMemberDetails implements UserDetails {

    private final LoginMember loginMember;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority(){
            @Override
            public String getAuthority(){
                return String.valueOf(loginMember.getRole());
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return loginMember.getPassword();
    }

    @Override
    public String getUsername() {
        return loginMember.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
