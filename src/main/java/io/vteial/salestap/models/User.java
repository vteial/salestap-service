package io.vteial.salestap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vteial.salestap.dtos.SessionDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ST_USERS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractModel {

    @GeneratedValue
    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "user_id")
    String userId;

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

    @Column(name = "token", nullable = true)
    String token;

    @Column(name = "status")
    String status;

    @Column(name = "role_id")
    String roleId;

//    transient Role role;
//
//    @Column(name = "node_id", nullable = true)
//    long nodeId;
//
//    @JsonIgnore
//    @ToString.Exclude
//    transient Node node;
//
//    public void setNode(Node node) {
//        this.node = node;
//        this.nodeId = node.getId();
//    }
//
//    @Column(name = "cash_account_id")
//    private long cashAccountId;
//
//    // @OneToOne
//    @JsonIgnore
//    @ToString.Exclude
//    private transient Account cashAccount;
//
//    public void setCashAccount(Account account) {
//        cashAccount = account;
//        cashAccountId = cashAccount.getId();
//    }
//
//    @Column(name = "profit_account_id")
//    private long profitAccountId;
//
//    // @OneToOne
//    @JsonIgnore
//    @ToString.Exclude
//    private transient Account profitAccount;
//
//    public void setProfitAccount(Account account) {
//        profitAccount = account;
//        profitAccountId = profitAccount.getId();
//    }
//
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

    @Column(name = "create_by")
    Long createBy;

    @Column(name = "update_by")
    Long updateBy;

    public void preUpdate(SessionDto sessionUser, Date now) {
        this.updateTime = now;
        if (sessionUser != null)
            this.updateBy = sessionUser.getId();
    }

    public void prePersist(SessionDto sessionUser, Date now) {
        this.createTime = now;
        this.updateTime = now;
        if (sessionUser != null) {
            this.createBy = sessionUser.getId();
            this.updateBy = sessionUser.getId();
        }
    }

    // domain operations
    public void correctData() {
    }

}