package io.vteial.salestap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.wybis.bigsales.dto.SessionDto;
import io.wybis.bigsales.model.constants.UserStatus;
import io.wybis.bigsales.model.constants.UserType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "USERS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractModel {

    static final String ID_KEY = "userId";

    @Id
    @Column(name = "id")
    String id;

    @JsonIgnore
    @Column(name = "password")
    String password;

    @Column(name = "email_id")
    String emailId;

    @Column(name = "mobile_no")
    String mobileNo;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "type")
    String type;

    @Column(name = "token")
    String token;

    @Column(name = "status")
    String status;

    @Column(name = "role_id")
    String roleId;

    transient Role role;

    @Column(name = "node_id", nullable = true)
    long nodeId;

    @JsonIgnore
    @ToString.Exclude
    transient Node node;

    public void setNode(Node node) {
        this.node = node;
        this.nodeId = node.getId();
    }

    @Column(name = "cash_account_id")
    private long cashAccountId;

    // @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private transient Account cashAccount;

    public void setCashAccount(Account account) {
        cashAccount = account;
        cashAccountId = cashAccount.getId();
    }

    @Column(name = "profit_account_id")
    private long profitAccountId;

    // @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private transient Account profitAccount;

    public void setProfitAccount(Account account) {
        profitAccount = account;
        profitAccountId = profitAccount.getId();
    }

//    @Column(name = "expenses_account_id")
//    private long expensesAccountId;
//
//    // @OneToOne
//    @JsonIgnore
//    @ToString.Exclude
//    private transient Account expensesAccount;
//
//    public void setExpensesAccount(Account account) {
//        expensesAccount = account;
//        profitAccountId = expensesAccount.getId();
//    }

    // common fields
    @Column(name = "create_time", nullable = false)
    Date createTime;

    @Column(name = "update_time", nullable = false)
    Date updateTime;

    @Column(name = "create_by", nullable = false)
    String createBy;

    @Column(name = "update_by", nullable = false)
    String updateBy;

    // persistance operations
    public void preUpdate(SessionDto sessionUser, Date now) {
        this.updateTime = now;
        this.updateBy = sessionUser.getUserId();
    }

    public void prePersist(SessionDto sessionUser, Date now) {
        this.createTime = now;
        this.updateTime = now;
        this.createBy = sessionUser.getUserId();
        this.updateBy = sessionUser.getUserId();
    }

    // domain operations
    public void correctData() {
        if (id != null) {
            id = id.toLowerCase();
        }
        if (emailId != null) {
            emailId = emailId.toLowerCase();
        } else {
            emailId = id;
        }
        if (firstName != null) {
            firstName = firstName.toLowerCase();
        }
        if (lastName != null) {
            lastName = lastName.toLowerCase();
        }
        if (status == null) {
            status = UserStatus.NEW;
        }
        if (type == null) {
            type = UserType.EMPLOYEE;
        }
        if (roleId == null) {
            roleId = Role.ID_EMPLOYEE;
        }
        if (role != null) {
            roleId = role.getId();
        }
        if (node != null) {
            nodeId = node.getId();
        }
        if (cashAccount != null) {
            cashAccountId = cashAccount.getId();
        }
        if (profitAccount != null) {
            profitAccountId = profitAccount.getId();
        }
//        if(expensesAccount != null) {
//            expensesAccountId = expensesAccount.getId();
//        }
    }

}