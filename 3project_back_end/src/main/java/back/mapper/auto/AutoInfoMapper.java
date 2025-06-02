package back.mapper.auto;

import org.apache.ibatis.annotations.Mapper;

import back.model.auto.AutoInfo;
@Mapper
public interface AutoInfoMapper {
	AutoInfo selectByEmail(String email);
    void insertAuthInfo(AutoInfo info);
    void updateAuthNumber(AutoInfo info);
    void verifyAuthEmail(String email);
}
