package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Layout_name")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"fragments","layouts"})
@EqualsAndHashCode(of = "lnName")
public class LayoutName extends BaseEntity<Integer>{

    @Column(name = "ln_name",nullable = false,unique = true)
    private String lnName;

    @Builder.Default
    @OneToMany(mappedBy = "layoutName")
    private List<Fragment> fragments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "layoutName")
    private List<Layout> layouts = new ArrayList<>();

    public void setFragments(List<Fragment> fragments){
        this.fragments = fragments;
        this.fragments.forEach(fragment -> fragment.setLayoutName(this));
    }

    public void setLayouts(List<Layout> layouts){
        this.layouts = layouts;
        this.layouts.forEach(layout->layout.setLayoutName(this));
    }

}
