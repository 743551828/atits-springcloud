package com.atits.base.properties;

import com.atits.base.utils.RsaUtils;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * JWT配置类
 *
 * @author: zhangys
 * @create: 2020-09-10 13:52
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@RefreshScope
public class JWTProperties {

    public static String publicSecretString;

    /**
     * 公钥KEY
     */
    public static PublicKey publicSecret;
    /**
     * TokenKey
     */
    public static String tokenHeader;
    /**
     * Token前缀字符
     */
    public static String tokenPrefix;

    public void setPublicSecretString(String publicSecretString) throws Exception {
        JWTProperties.publicSecret = RsaUtils.getPublicKeyByString(publicSecretString);
    }

    public void setTokenHeader(String tokenHeader) {
        JWTProperties.tokenHeader = tokenHeader;
    }

    public void setTokenPrefix(String tokenPrefix) {
        JWTProperties.tokenPrefix = tokenPrefix;
    }

}