package com.kuznetsov.linoleumShopRest.entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
public abstract class BaseEntity<T extends Serializable>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

}
