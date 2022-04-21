package io.vteial.salestap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.wybis.bigsales.dto.SessionDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "NODE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Node extends AbstractModel {

    public static final String ID_KEY = "nodeId";

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "alias_name", nullable = true)
    private String aliasName;

    @Column(name = "parent_id")
    private long parentId;

    @Column(name = "status")
    private String status;

    @Column(name = "user_id")
    private String userId;

    @JsonIgnore
    @ToString.Exclude
    private transient User user;

    public void setUser(User user) {
        this.user = user;
        userId = user.getId();
    }

//    @Column(name = "account_id", nullable = true)
//    private long accountId;

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

    //domain operations
    public void correctData() {
        if (code != null) {
            code = code.toLowerCase();
        }
        if (user != null) {
            userId = user.getId();
        }
    }
}