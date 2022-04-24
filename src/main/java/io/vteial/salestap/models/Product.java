package io.vteial.salestap.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vteial.salestap.dtos.SessionDto;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ST_PRODUCT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class Product extends AbstractModel {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "buy_rate")
    private double buyRate;

    @Column(name = "buy_percent")
    private double buyPercent;

    @Column(name = "sell_rate")
    private double sellRate;

    @Column(name = "sell_percent")
    private double sellPercent;

    @Column(name = "base_unit")
    private double baseUnit;

    @Column(name = "denominator")
    private double denominator;

    @Column(name = "hand_stock")
    private double handStock;

    @Column(name = "hand_stock_average")
    private double handStockAverage;

    @Column(name = "amount")
    private double amount;

    @Column(name = "status")
    private String status;

    @Column(name = "shop_id")
    private Long shopId;

//    @OneToOne
//    @JsonIgnore
//    @ToString.Exclude
//    private transient Shop shop;
//
//    public void setNode(Shop shop) {
//        this.shop = shop;
//        shopId = this.shop.getId();
//    }

    @Column(name = "account_id")
    private long accountId;

//    @OneToOne
//    @JsonIgnore
//    @ToString.Exclude
//    private transient Account account;
//
//    public void setAccount(Account account) {
//        this.account = account;
//        accountId = this.account.getId();
//    }

    // common fields
    @Column(name = "create_time", nullable = false)
    protected Date createTime;

    @Column(name = "update_time", nullable = false)
    protected Date updateTime;

    @Column(name = "create_by", nullable = false)
    protected Long createBy;

    @Column(name = "update_by", nullable = false)
    protected Long updateBy;

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

    // domain operations
    public void correctData() {
//        if (shop != null) {
//            shopId = shop.getId();
//        }
    }

    public double getBuyPercentageRate() {
        double value = buyRate * (buyPercent / 100);
        value = buyRate + value;
        return value;
    }

    public double getSellPercentageRate() {
        double value = sellRate * (sellPercent / 100);
        value = sellRate - value;
        return value;
    }

    public boolean isRateIsAllowableForBuy(double rate) {
        double bpr = getBuyPercentageRate();
        return rate <= bpr;
    }

    public boolean isRateIsAllowableForSell(double rate) {
        double spr = getSellPercentageRate();
        return rate >= spr;
    }

    public boolean hasSufficientHandStock(double unit) {
        return unit <= handStock;
    }

    public void withdrawHandStock(double unit) {
        handStock -= unit;
        this.computeAmount();
    }

    public void depositHandStock(double unit) {
        handStock += unit;
        this.computeAmount();
    }

    private void computeAmount() {
        amount = handStock * (handStockAverage / baseUnit);
    }

    public void computeHandStockAverage(double unit, double rate) {
        // println("unit = " + unit)
        // println("rate = " + rate)
        // println("total = " + this.handStock)
        double value1 = this.handStock * (this.handStockAverage / this.baseUnit);
        // println("value1 = " + value1)
        double value2 = unit * (rate / this.baseUnit);
        // println("value2 = " + value2)
        double value3 = value1 + value2;
        // println("value3 = " + value3)
        double value4 = this.handStock + unit;
        // println("value4 = " + value4)
        double value5 = (value3 / value4) * this.baseUnit;
        // println("value5 = " + value5)
        this.handStockAverage = value5;
    }

    public void revertHandStockAverage(double unit, double rate) {
        double hst = this.handStock - unit;
        if (hst > 0) {
            // println("unit = " + unit)
            // println("rate = " + rate)
            // println("total = " + this.handStock)
            double value1 = this.handStock * (this.handStockAverage / this.baseUnit);
            // println("value1 = " + value1)
            double value2 = unit * (rate / this.baseUnit);
            // println("value2 = " + value2)
            double value3 = value1 - value2;
            // println("value3 = " + value3)
            double value4 = this.handStock - unit;
            // println("value4 = " + value4)
            double value5 = (value3 / value4) * this.baseUnit;
            // println("value5 = " + value5)
            this.handStockAverage = value5;
        }
    }
}
