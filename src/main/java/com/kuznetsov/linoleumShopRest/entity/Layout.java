package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"orders"})
@EqualsAndHashCode(exclude = {"layoutName","address"}, callSuper = true)
public class Layout extends BaseEntity<Integer>{

    @Column(name = "room_count")
    private Integer roomCount;

    @Column(name = "layout_img")
    private String layoutImg;

    @Column(name = "row_type")
    private LayoutRowType rowType;

    @Column(name = "l_type")
    private LayoutType lType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_name_id",referencedColumnName = "id")
    private LayoutName layoutName;

    @Builder.Default
    @OneToMany(mappedBy = "layout")
    private List<OrderWithLayout> orders = new ArrayList<>();

    public void setAddress(Address address){
        this.address = address;
        this.address.getLayouts().add(this);
    }

    public void setLayoutName(LayoutName layoutName){
        this.layoutName = layoutName;
        this.layoutName.getLayouts().add(this);
    }

    public void setOrders(List<OrderWithLayout> orders){
        this.orders = orders;
        this.orders.forEach(order -> order.setLayout(this));
    }

}
