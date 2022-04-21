package io.vteial.salestap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UserDto implements Serializable {

    long id;

    String recaptchaValue;

    String userId;

    String password;

    String retypePassword;

    String emailId;

    String firstName;

    String lastName;

    String currentPassword;

    String newPassword;

    String retypeNewPassword;

}
