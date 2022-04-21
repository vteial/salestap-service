package io.vteial.salestap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "TRAN")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Tran extends AbstractModel {

    public static final String ID_KEY = "tranId";

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_code")
    private String productCode;

    @JsonIgnore
    private transient Product product;

    @Column(name = "type")
    private String type;

    @Column(name = "base_unit")
    double baseUnit;

    @Column(name = "unit")
    double unit;

    @Column(name = "balance_unit")
    double balanceUnit;

    @Column(name = "rate")
    double rate;

    @Column(name = "averate_rate")
    double averageRate;

    @Column(name = "amount")
    private double amount;

    @Column(name = "balance_amount")
    private double balanceAmount;

    @Column(name = "tr_date")
    private Date date;

    @Column(name = "status")
    private String status;

    @Column(name = "category")
    private String category;

    @Column(name = "ref_text")
    private String refText;

    @Column(name = "description")
    private String description;

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

    @Column(name = "receipt_id")
    private long receiptId;

    @JsonIgnore
    private transient TranReceipt receipt;

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
        productCode = product.getCode();
        baseUnit = product.getBaseUnit();
        averageRate = product.getHandStockAverage();
        balanceUnit = product.getAccount().getHandStock();
        balanceAmount = product.getAccount().getAmount();
    }

    public void resetEntityReference() {
        product = null;
        forUser = null;
        byUser = null;
        node = null;
        receipt = null;
    }
}
