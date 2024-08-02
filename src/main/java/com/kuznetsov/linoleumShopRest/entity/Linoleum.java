package com.kuznetsov.linoleumShopRest.entity;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"rolls","orders"})
@EqualsAndHashCode(of = "lName")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED,withModifiedFlag = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
//Используется Spring cache
//NONSTRICT_READ_WRITE так как данные обновляются редко(каталог линолеумов)
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,region = "Linoleums")
public class Linoleum extends AuditingEntity<Integer> {

    @Column(name = "l_name",nullable = false,unique = true)
    private String lName;

    private Float protect;

    private Float thickness;

    private Integer price;

    @Column(name = "image_path")
    private String imagePath;


    @Builder.Default
    @OneToMany(mappedBy = "linoleum",cascade = CascadeType.ALL,orphanRemoval = true)
    @NotAudited
    private List<Roll> rolls = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "linoleum",cascade = CascadeType.ALL,orphanRemoval = true)
    @NotAudited
    private List<Order> orders = new ArrayList<>();

    public void setRolls(List<Roll> rolls){
        this.rolls = rolls;
        rolls.forEach(roll->roll.setLinoleum(this));
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
        this.orders.forEach(roll->roll.setLinoleum(this));
    }


}
