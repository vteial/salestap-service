package io.vteial.salestap.models;

import io.vteial.salestap.dtos.SessionDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "APP_CONFIG")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class AppConfig extends AbstractModel {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "key", nullable = false)
    String key;

    @Column(name = "value", nullable = false)
    String value;

//    @Column(name = "for_user_id")
//    Long forUserId;

    @Column(name = "create_time", nullable = false)
    protected Date createTime;

    @Column(name = "update_time", nullable = false)
    protected Date updateTime;

    @Column(name = "create_by")
    protected Long createBy;

    @Column(name = "update_by")
    protected Long updateBy;

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

}
