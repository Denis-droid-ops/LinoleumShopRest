package com.kuznetsov.linoleumShopRest.util;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicates {

    private final List<Predicate> predicateList = new ArrayList<>();

    public static QPredicates builder(){
        return new QPredicates();
    }

    public <T> QPredicates add(T object, Function<T,Predicate> function){
        if(object!=null){
            predicateList.add(function.apply(object));
        }
        return this;
    }

    public <T,K> QPredicates add(T object1, K object2,BiFunction<T,K,Predicate> function){
        if(object1!=null && object2!=null){
            predicateList.add(function.apply(object1,object2));
        }
        return this;
    }

    public Predicate build(){
        return Optional.ofNullable(ExpressionUtils.allOf(predicateList))
                .orElseGet(()->Expressions.asBoolean(true).isTrue());
    }

    public Predicate buildOr(){
        return Optional.ofNullable(ExpressionUtils.anyOf(predicateList))
                .orElseGet(()->Expressions.asBoolean(true).isTrue());
    }
}
