package com.kuznetsov.linoleumShopRest.dto;

import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.entity.Revision;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.envers.RevisionType;

@Value
@Getter
@RequiredArgsConstructor
public class RevisionDto {

    Linoleum linoleum;
    Revision revision;
    RevisionType revisionType;

}
