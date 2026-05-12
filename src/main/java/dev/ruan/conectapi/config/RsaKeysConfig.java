package dev.ruan.conectapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RsaKeysConfig {

    @Value("classpath:public.key")
    private Resource publicKeyResource;

    @Value("classpath:private.key")
    private Resource privateKeyResource;

    @Bean
    public RSAPublicKey rsaPublicKey() throws Exception {
        return loadPublicKey();
    }

    @Bean
    public RSAPrivateKey rsaPrivateKey() throws Exception {
        return loadPrivateKey();
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        String key = loadKey(publicKeyResource);
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) factory.generatePublic(spec);
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        String key = loadKey(privateKeyResource);
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) factory.generatePrivate(spec);
    }

    private String loadKey(Resource resource) throws IOException {
        return resource.getContentAsString(StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }
}