package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "ORDER_WITH_DELIVERY_ADDRESS")
public class OrderWithDeliveryAddress extends Order{

    @Column(name = "apartment_num")
    private Integer apartmentNum;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id",referencedColumnName = "id")
    private Address address;

    @Builder(builderMethodName = "childBuilder")
    public OrderWithDeliveryAddress(OrderStatus status,
                                    OrderTransporting transportingType,
                                    LocalDateTime transportingDate,
                                    Integer cost,
                                    User user,
                                    Linoleum linoleum,
                                    List<FragmentOrder> fragmentOrders,
                                    Integer apartmentNum,
                                    Address address) {
        super(status, transportingType, transportingDate, cost, user, linoleum, fragmentOrders);
        this.apartmentNum = apartmentNum;
        this.address = address;
    }

    public void setAddress(Address address){
        this.address = address;
        this.address.getOrders().add(this);
    }
}
