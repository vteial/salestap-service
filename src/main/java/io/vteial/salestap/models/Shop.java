package io.vteial.salestap.models;

import io.vteial.salestap.dtos.SessionDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ST_SHOP")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Shop extends AbstractModel {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alias_name", nullable = true)
    private String aliasName;

    @Column(name = "parent_id", nullable = false)
    private long parentId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "user_id", nullable = false)
    private Long userId;

//    @JsonIgnore
//    @ToString.Exclude
//    private transient User user;
//
//    public void setUser(User user) {
//        this.user = user;
//        userId = user.getId();
//    }

//    @Column(name = "account_id", nullable = true)
//    private long accountId;

    // common fields
    @Column(name = "create_time", nullable = false)
    Date createTime;

    @Column(name = "update_time", nullable = false)
    Date updateTime;

    @Column(name = "create_by")
    Long createBy;

    @Column(name = "update_by")
    Long updateBy;

    // persistance operations
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

    //domain operations
    public void correctData() {
        if (code != null) {
            code = code.toLowerCase();
        }
    }
}
