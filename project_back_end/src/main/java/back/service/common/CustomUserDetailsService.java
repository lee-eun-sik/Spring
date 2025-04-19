package back.service.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import back.mapper.user.UserMapper;
import back.model.common.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
    private UserMapper userMapper;

  
    @Override // userId넘겨받음
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userMapper.getUserById(username); // 데이터 user받음
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return new CustomUserDetails(user);//모델 생성자에 뭔가 넣고 처리
    }
}
