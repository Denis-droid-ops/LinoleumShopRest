package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "fragment_order")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = {"fragment","order"},callSuper = true)
public class FragmentOrder extends BaseEntity<Integer>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fragment_id",referencedColumnName = "id")
    private Fragment fragment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    public void setFragment(Fragment fragment){
        this.fragment = fragment;
        this.fragment.getFragmentOrders().add(this);
    }

    public void setOrder(Order order){
        this.order = order;
        this.order.getFragmentOrders().add(this);
    }
}
