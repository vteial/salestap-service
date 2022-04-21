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
@Table(name = "account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends AbstractModel {

    public static final String ID_KEY = "accountId";

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "alias_name")
    private String aliasName;

    @Column(name = "type")
    private String type;

    @Column(name = "is_minus")
    private boolean minus;

    @Column(name = "amount")
    private double amount;

    @Column(name = "hand_stock")
    private double handStock;

    @Column(name = "status")
    private String status;

    @Column(name = "product_id", nullable = true)
    private long productId;

    // @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private transient Product product;

    public void setProduct(Product product) {
        this.product = product;
        productId = this.product.getId();
    }

    @Column(name = "node_id")
    private long nodeId;

    // @OneToOne(mappedBy = "account")
    @JsonIgnore
    @ToString.Exclude
    private transient Node node;

    public void setNode(Node node) {
        this.node = node;
        nodeId = this.nodeId;
    }

    @Column(name = "user_id", nullable = true)
    private String userId;

    // @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private transient User user;

    public void setUser(User user) {
        this.user = user;
        userId = this.user.getId();
    }

    @Column(name = "create_time", nullable = false)
    protected Date createTime;

    @Column(name = "update_time", nullable = false)
    protected Date updateTime;

    @Column(name = "create_by", nullable = false)
    protected String createBy;

    @Column(name = "update_by", nullable = false)
    protected String updateBy;

    // persistence operations
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
        if (product != null) {
            productId = product.getId();
        }
        if (node != null) {
            nodeId = node.getId();
        }
        if (user != null) {
            userId = user.getId();
        }
        if (name == null) {
            name = type + "-" + userId + "-" + nodeId;
        }
        name = name.toLowerCase();
        if (aliasName == null) {
            aliasName = type + "-" + userId;
            if (node != null) {
                aliasName = aliasName + "-" + node.getCode();
            }
        }
        aliasName = aliasName.toLowerCase();
    }

    public boolean hasSufficientHandStock(double unit) {
        return minus ? true : unit <= handStock;
    }

    public void withdrawHandStock(double unit) {
        handStock -= unit;
        computeAmount();
        product.withdrawHandStock(unit);
    }

    public void depositHandStock(double unit) {
        handStock += unit;
        computeAmount();
        product.depositHandStock(unit);
    }

//    public boolean hasSufficientAmount(double amount) {
//        return minus || amount <= this.amount;
//    }
//
//    public void withdrawAmount(double amount) {
//        this.amount -= amount;
//    }
//
//    public void depositAmount(double amount) {
//        this.amount += amount;
//    }

    void computeAmount() {
        amount = handStock * (product.getHandStockAverage() / product.getBaseUnit());
    }
}
