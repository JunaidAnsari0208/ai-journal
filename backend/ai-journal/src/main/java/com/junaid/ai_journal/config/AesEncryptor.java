package com.junaid.ai_journal.config;

import com.junaid.ai_journal.service.impl.JournalServiceImpl;
import jakarta.persistence.AttributeConverter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Configuration
public class AesEncryptor implements AttributeConverter<Object, String> {

    @Value("${aes.encryption.key}")
    private  String encryptionKey;
    private final String encryptionCipher = "AES";
    private static final Logger logger = LoggerFactory.getLogger(AesEncryptor.class);

    private Key key;
    private Cipher cipher;

    private Key getKey() {
        if(key == null)
            key = new SecretKeySpec(encryptionKey.getBytes(), encryptionCipher);
        return key;
    }

    private Cipher getCipher() throws GeneralSecurityException {
        if(cipher == null)
            cipher = Cipher.getInstance(encryptionCipher);
        return cipher;
    }

    private void initCipher(int encryptMode) throws GeneralSecurityException {
        getCipher().init(encryptMode, getKey());
    }


    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if(attribute == null)
            return null;
        initCipher(Cipher.ENCRYPT_MODE);
        byte[] bytes = SerializationUtils.serialize(attribute);
        return Base64.getEncoder().encodeToString(getCipher().doFinal(bytes));
    }

    @SneakyThrows
    @Override
    public Object convertToEntityAttribute(String dbData) {
        if(dbData == null)
            return null;
        try {
            initCipher(Cipher.DECRYPT_MODE);
            byte[] bytes = getCipher().doFinal(Base64.getDecoder().decode(dbData));
            return SerializationUtils.deserialize(bytes);
        }catch (Exception e){
            return dbData;
        }
    }
}
