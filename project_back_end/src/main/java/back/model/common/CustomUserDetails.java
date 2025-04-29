package back.model.common;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import back.model.user.User;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails { //받아서 재정의함

    private final User user; // 생성자써서

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user; // 컨트롤러에서 getUser()로 원래 모델 꺼낼 수 있음
    }

    @Override
    public String getUsername() {
        return user.getUserId(); // 암호화 Spring 보안이 체크한다. 
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 관리 필요시 여기에 추가
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
