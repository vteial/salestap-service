package io.vteial.salestap.controllers;

import io.vteial.salestap.dtos.ResponseDto;
import io.vteial.salestap.utils.Helper;
import io.vteial.salestap.utils.JasyptConfigSource;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;

@Slf4j
@Path("/jasypt")
@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
public class JasyptController {

    private static final String ENCRYPTED_VALUE_PREFIX = "ENC(";
    private static final String ENCRYPTED_VALUE_SUFFIX = ")";

    private StringEncryptor stringEncryptor;

    static boolean isEncryptedValue(final String value) {
        if (value == null) {
            return false;
        }
        final String trimmedValue = value.trim();
        return (trimmedValue.startsWith(ENCRYPTED_VALUE_PREFIX) &&
                trimmedValue.endsWith(ENCRYPTED_VALUE_SUFFIX));
    }

    private static String getInnerEncryptedValue(final String value) {
        return value.substring(
                ENCRYPTED_VALUE_PREFIX.length(),
                (value.length() - ENCRYPTED_VALUE_SUFFIX.length()));
    }

    @PostConstruct
    void init() {
        stringEncryptor = new JasyptConfigSource().getEncryptor();
    }

    @GET
    @Path("/encrypt/{text}")
    public ResponseDto encrypt(@PathParam("text") String text) {
        ResponseDto responseDto = ResponseDto.builder().build();

        try {
            log.debug("text : {}", text);
            String encrypted = stringEncryptor.encrypt(text.trim());
            if (log.isDebugEnabled()) {
                log.debug("ORIGINAL  : {}", text);
                log.debug("ENCRYPTED : {}", encrypted);
                log.debug("DECRYPTED : {}", stringEncryptor.decrypt(encrypted));
            }
            responseDto.setData(String.format("ENC(%s)", encrypted));
        } catch (Throwable t) {
            log.error("encryption failed", t);
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage("encryption failed...");
            responseDto.setData(Helper.getStackTraceAsString(t));
        }

        return responseDto;
    }

    @GET
    @Path("/decrypt/{text}")
    public ResponseDto decrypt(@PathParam("text") String text) {
        ResponseDto responseDto = ResponseDto.builder().build();
        ;

        try {
            log.debug("text : {}", text);
            String decrypted = stringEncryptor.decrypt(isEncryptedValue(text) ? getInnerEncryptedValue(text) : text);
            if (log.isDebugEnabled()) {
                log.debug("ORIGINAL  : {}", text);
                log.debug("DECRYPTED : {}", decrypted);
                log.debug("ENCRYPTED : {}", String.format("ENC(%s)", stringEncryptor.encrypt(decrypted)));
            }
            responseDto.setData(decrypted);
        } catch (Throwable t) {
            log.error("decryption failed", t);
            responseDto.setType(ResponseDto.ERROR);
            responseDto.setMessage("decryption failed...");
            responseDto.setData(Helper.getStackTraceAsString(t));
        }

        return responseDto;
    }

}
