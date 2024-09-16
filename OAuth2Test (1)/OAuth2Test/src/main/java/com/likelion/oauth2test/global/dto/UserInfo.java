package com.likelion.oauth2test.global.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class UserInfo { // 구글 accessToken으로 사용자 정보를 받는 클래스
    private String id;
    private String email;
    @SerializedName("verified_email")
    private Boolean verifiedEmail;
    private String name;
    @SerializedName("given_name")
    private String givenName;
    @SerializedName("family_name")
    private String familyName;
    @SerializedName("picture")
    private String pictureUrl;
    private String locale;
}