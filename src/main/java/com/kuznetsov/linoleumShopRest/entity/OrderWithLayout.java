package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "ORDER_WITH_LAYOUT")
public class OrderWithLayout extends Order{

    @Column(name = "apartment_num")
    private Integer apartmentNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id",referencedColumnName = "id")
    private Layout layout;

    @Builder(builderMethodName = "childBuilder")
    public OrderWithLayout(OrderStatus status,
                           OrderTransporting transportingType,
                           LocalDateTime transportingDate,
                           Integer cost,
                           User user,
                           Linoleum linoleum,
                           List<FragmentOrder> fragmentOrders,
                           Integer apartmentNum,
                           Layout layout) {
        super(status, transportingType, transportingDate, cost, user, linoleum, fragmentOrders);
        this.apartmentNum = apartmentNum;
        this.layout = layout;
    }


    public void setLayout(Layout layout){
        this.layout = layout;
        this.layout.getOrders().add(this);
    }
}
