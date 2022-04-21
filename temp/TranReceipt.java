package io.vteial.salestap.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TRAN_RECEIPT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class TranReceipt extends AbstractModel {

    public static final String ID_KEY = "tranReceiptId";

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "ref_text")
    private String refText;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "received_amount")
    private double receivedAmount;

    @Column(name = "balance_amount")
    private double balanceAmount;

    @Column(name = "tr_date")
    private Date date;

    @Column(name = "status")
    private String status;

    @Column(name = "for_user_id")
    private String forUserId;

    @JsonIgnore
    private transient User forUser;

    @Column(name = "by_user_id")
    private String byUserId;

    @JsonIgnore
    private transient User byUser;

    @Column(name = "node_id")
    private long nodeId;

    @JsonIgnore
    private transient Node node;

    private transient List<Tran> trans;

    // persistence operations
    public void preUpdate(String updateBy) {
        this.updateBy = updateBy;
        this.updateTime = new Date();
    }

    public void prePersist(String createAndUpdateBy) {
        this.createBy = createAndUpdateBy;
        this.updateBy = createAndUpdateBy;
        Date now = new Date();
        this.createTime = now;
        this.updateTime = now;
    }

    @Column(name = "create_time", nullable = false)
    protected Date createTime;

    @Column(name = "update_time", nullable = false)
    protected Date updateTime;

    @Column(name = "create_by", nullable = false)
    protected String createBy;

    @Column(name = "update_by", nullable = false)
    protected String updateBy;

    // domain operations
    public void correctData() {
    }

    public void resetEntityReference() {
        forUser = null;
        byUser = null;
        node = null;
    }
}
