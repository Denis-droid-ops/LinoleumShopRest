package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = "orders")
@EqualsAndHashCode(of = "email")
public class User extends BaseEntity<Integer>{

    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    private String password;

    @Column(name = "phone_number")
    private long phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    public void setOrders(List<Order> orders){
        this.orders = orders;
        this.orders.forEach(order -> order.setUser(this));
    }
}
