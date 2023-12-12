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
@ToString(exclude = {"fragmentOrders"})
@EqualsAndHashCode(exclude = "layoutName", callSuper = true)
public class Fragment extends BaseEntity<Integer>{

    @Column(name = "f_width")
    private Float fWidth;

    @Column(name = "f_length")
    private Float fLength;

    @Column(name = "f_type")
    @Enumerated(value = EnumType.STRING)
    private FragmentType fType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_name_id",referencedColumnName = "id")
    private LayoutName layoutName;

    @Builder.Default
    @OneToMany(mappedBy = "fragment")
    private List<FragmentOrder> fragmentOrders = new ArrayList<>();

    public void setLayoutName(LayoutName layoutName){
        this.layoutName = layoutName;
        this.layoutName.getFragments().add(this);
    }
}
