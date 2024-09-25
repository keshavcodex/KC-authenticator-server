package com.kc.authenticator.services;

import com.kc.authenticator.dto.DecryptedData;
import com.kc.authenticator.model.Token;
import com.kc.authenticator.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Service
public class TokenService {

    @Value("${app.tokenSecretKey}")
    private String secretKey;

    @Value("${app.tokenDuration}")
    private Integer tokenDuration;

    @Autowired
    private TokenRepository tokenRepository;

    public String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public String generateEncryptedIdAndToken(String referenceId) throws Exception {
        String generatedToken = generateToken();
        Token tokenDoc = new Token(referenceId, generatedToken, tokenDuration);
        String id = saveToken(tokenDoc).getId();
        String combined = id + ":" + generatedToken;  // id and token;
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(combined.getBytes(StandardCharsets.UTF_8));
        String encryptedToken = Base64.getEncoder().encodeToString(encryptedBytes);
        String urlEncodedToken = URLEncoder.encode(encryptedToken, StandardCharsets.UTF_8.toString());
        return urlEncodedToken;
    }

    // Decrypt the email and token
    public DecryptedData decryptIdAndToken(String encryptedData) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            if (decodedBytes.length % 16 != 0) {
                return new DecryptedData(false); // Return invalid data response
            }
            String data = new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
            Token token = new Token(data.split(":"));
            return new DecryptedData(token.getId(), token.getToken());
        } catch (Exception e) {
            System.out.println(e);
            return new DecryptedData(false);
        }
    }

    public Boolean isTokenValid(String id) {
        Optional<Token> tokenDoc = tokenRepository.findById(id);
        return tokenDoc.isPresent();
    }

    public Optional<Token> getTokenAndDeleteToken(String id) {
        Optional<Token> tokenOptional = tokenRepository.findById(id);
        if (tokenOptional.isPresent()) {
            tokenRepository.deleteById(id); // Delete the token if found
        }
        return tokenOptional; // Return the token whether found or not
    }

//    public List<Token> getAllTokens() {
//        List<Token> tokens = tokenRepository.findAll();
//        return tokens;
//    }

}
