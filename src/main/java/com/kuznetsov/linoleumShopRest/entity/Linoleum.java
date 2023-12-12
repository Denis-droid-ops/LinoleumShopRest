package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"rolls","orders"})
@EqualsAndHashCode(of = "lName")
public class Linoleum extends BaseEntity<Integer>{

    @Column(name = "l_name",nullable = false,unique = true)
    private String lName;

    private Float protect;

    private Float thickness;

    private Integer price;

    @Column(name = "image_path")
    private String imagePath;

    @Builder.Default
    @OneToMany(mappedBy = "linoleum")
    private List<Roll> rolls = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "linoleum")
    private List<Order> orders = new ArrayList<>();

    private void setRolls(List<Roll> rolls){
        this.rolls = rolls;
        rolls.forEach(roll->roll.setLinoleum(this));
    }

    private void setOrders(List<Order> orders){
        this.orders = orders;
        this.orders.forEach(roll->roll.setLinoleum(this));
    }

}
