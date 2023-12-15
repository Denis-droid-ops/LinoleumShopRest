package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@EqualsAndHashCode(exclude = "linoleum",callSuper = true)
public class Roll extends AuditingEntity<Integer>{

    @Column(name = "part_num")
    private Integer partNum;

    @Column(name = "r_width")
    private Float rWidth;

    @Column(name = "r_length")
    private Float rLength;

    @Column(name = "is_remain")
    private Boolean isRemain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linoleum_id",referencedColumnName = "id")
    private Linoleum linoleum;

    public void setLinoleum(Linoleum linoleum){
        this.linoleum = linoleum;
        this.linoleum.getRolls().add(this);
    }
}
