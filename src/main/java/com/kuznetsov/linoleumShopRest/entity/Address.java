package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"layouts","orders"})
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseEntity<Integer>{

    private String city;

    private String street;

    @Column(name = "home_num")
    private String homeNum;

    @Builder.Default
    @OneToMany(mappedBy = "address")
    private List<Layout> layouts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "address")
    private List<Order> orders = new ArrayList<>();

    public void setOrders(List<Order> orders){
        this.orders = orders;
        this.orders.forEach(order->order.setAddress(this));
    }

    public void setLayouts(List<Layout> layouts){
        this.layouts = layouts;
        this.layouts.forEach(layout -> layout.setAddress(this));
    }
}
