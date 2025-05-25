// back/mapper/AuthMapper.java
package back.mapper.auth;

import org.apache.ibatis.annotations.Mapper;

import back.model.user.User;

@Mapper
public interface AuthMapper {
    User findByNaverId(String naverId);
    void insertNaverUser(User user);
    
    User findByKakaoId(String kakaoId);
    void insertKakaoUser(User user);
}
