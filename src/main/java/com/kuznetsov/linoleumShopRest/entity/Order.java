package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"fragmentOrders"})
@EqualsAndHashCode(exclude = {"user","linoleum","address","layout","fragmentOrders"},callSuper = true)
public class Order extends BaseEntity<Integer>{

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "transporting_type")
    private OrderTransporting transportingType;

    @Column(name = "transporting_date")
    private LocalDateTime transportingDate;

    private Integer cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linoleum_id",referencedColumnName = "id")
    private Linoleum linoleum;

    @Column(name = "apartment_num")
    private Integer apartmentNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id",referencedColumnName = "id")
    private Layout layout;

    @Enumerated(value = EnumType.STRING)
    private OrderType type;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<FragmentOrder> fragmentOrders = new ArrayList<>();

    public void setUser(User user){
        this.user = user;
        this.user.getOrders().add(this);
    }

    public void setLinoleum(Linoleum linoleum){
        this.linoleum = linoleum;
        this.linoleum.getOrders().add(this);
    }

    public void setAddress(Address address){
        this.address = address;
        this.address.getOrders().add(this);
    }

    public void setLayout(Layout layout){
        this.layout = layout;
        this.layout.getOrders().add(this);
    }


}
