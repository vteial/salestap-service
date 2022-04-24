package io.vteial.salestap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vteial.salestap.dtos.SessionDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ST_ACCOUNT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends AbstractModel {

    @GeneratedValue
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

//    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private transient Product product;

    public void setProduct(Product product) {
        this.product = product;
        productId = this.product.getId();
    }

    @Column(name = "shop_id")
    private Long shopId;

//    @OneToOne(mappedBy = "account")
//    @JsonIgnore
//    @ToString.Exclude
//    private transient Shop shop;
//
//    public void setNode(Shop shop) {
//        this.shop = shop;
//        shopId = this.shopId;
//    }

    @Column(name = "user_id", nullable = true)
    private Long userId;

//    @OneToOne
//    @JsonIgnore
//    @ToString.Exclude
//    private transient User user;
//
//    public void setUser(User user) {
//        this.user = user;
//        userId = this.user.getId();
//    }

    @Column(name = "create_time", nullable = false)
    protected Date createTime;

    @Column(name = "update_time", nullable = false)
    protected Date updateTime;

    @Column(name = "create_by", nullable = false)
    protected Long createBy;

    @Column(name = "update_by", nullable = false)
    protected Long updateBy;

    // persistence operations
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
        if (product != null) {
            productId = product.getId();
        }
//        if (shop != null) {
//            shopId = shop.getId();
//        }
//        if (user != null) {
//            userId = user.getUserId();
//        }
//        if (name == null) {
//            name = type + "-" + userId + "-" + shopId;
//        }
//        name = name.toLowerCase();
//        if (aliasName == null) {
//            aliasName = type + "-" + userId;
//            if (shop != null) {
//                aliasName = aliasName + "-" + shop.getCode();
//            }
//        }
//        aliasName = aliasName.toLowerCase();
    }

    public boolean hasSufficientHandStock(double unit) {
        return minus || unit <= handStock;
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
