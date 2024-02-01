package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"rolls","orders"})
@EqualsAndHashCode(of = "lName")
//Use Spring cache
//NONSTRICT_READ_WRITE так как данные обновляются редко(каталог линолеумов)
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region = "Linoleums")
public class Linoleum extends AuditingEntity<Integer>{

    @Column(name = "l_name",nullable = false,unique = true)
    private String lName;

    private Float protect;

    private Float thickness;

    private Integer price;

    @Column(name = "image_path")
    private String imagePath;

    @Builder.Default
    @OneToMany(mappedBy = "linoleum",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Roll> rolls = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "linoleum",cascade = CascadeType.ALL,orphanRemoval = true)
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
