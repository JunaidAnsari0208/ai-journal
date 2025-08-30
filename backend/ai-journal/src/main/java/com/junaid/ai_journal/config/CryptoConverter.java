package com.junaid.ai_journal.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Override
    public String convertToDatabaseColumn(String attribute){

        if(attribute == null){
            return null;
        }
        return stringEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData){
        if (dbData == null) {
            return null;
        }
        return stringEncryptor.decrypt(dbData);
    }
}
