package com.kuznetsov.linoleumShopRest.dto;

import com.kuznetsov.linoleumShopRest.entity.Linoleum;
import com.kuznetsov.linoleumShopRest.entity.Revision;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.envers.RevisionType;

@Value
@Getter
@RequiredArgsConstructor
@Schema(description = "Information about revision(auditing)")
public class RevisionDto {

    Linoleum linoleum;
    Revision revision;
    RevisionType revisionType;

}
