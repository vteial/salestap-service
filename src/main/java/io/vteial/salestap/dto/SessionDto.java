package io.vteial.salestap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class SessionDto implements Serializable {

    long id;

    String userId;

    String emailId;

    String firstName;

    String lastName;

    String type;

    String roleId;

//    long cashAccountId;
//
//    Account cashAccount;
//
//    long profitAccountId;
//
//    Account profitAccount;
//
//    long branchId;
//
//    String branchCode;
//
//    String branchName;
//
//    long branchVirtualEmployeeId;

}
