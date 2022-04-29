package io.vteial.salestap.models;

import io.vteial.salestap.dtos.SessionDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "ST_SHOP")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Shop extends AbstractModel {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 6, message = "Code should have size [{min},{max}]")
    @NotBlank(message = "Code should not be blank")
    @Column(name = "code", nullable = false)
    private String code;

    @Size(min = 3, max = 30, message = "Name should have size [{min},{max}]")
    @NotBlank(message = "Name should not be blank")
    // @Pattern(regexp = "^[a-zA-Z]+$", message = "Name should start with a letter and should only have letters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(min = 3, max = 30, message = "Alias Name should have size [{min},{max}]")
    @Column(name = "alias_name", nullable = true)
    private String aliasName;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

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
