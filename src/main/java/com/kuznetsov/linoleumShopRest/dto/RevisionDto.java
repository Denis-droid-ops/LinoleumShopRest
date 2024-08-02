package com.kuznetsov.linoleumShopRest.dto;

import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.entity.Revision;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.envers.RevisionType;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Getter
@RequiredArgsConstructor
public class RevisionDto {

    String user;
    Long timestamp;
    RevisionType revisionType;
    Map<String,Object> changedFields;

}
