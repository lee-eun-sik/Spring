package back.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NaverUserResponse {

    @JsonProperty("resultcode")
    private String resultCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("response")
    private NaverUserInfo response;
}
