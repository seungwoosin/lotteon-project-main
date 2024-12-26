package com.lotteon.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetConfigDTO {
    private Long id;

    private String configVersion;

    private String configTitle;

    private String configSub;

    private String configHeaderLogo;

    private String configFooterLogo;

    private String configFabicon;

    private int configUpdateVersion;

    private int configUpdateLocation;

    private String configUpdatedAdmin;

    private Timestamp configCreatedAt;

    private boolean configIsUsed;

    private String createdStr;



    public void setCreatedStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
        createdStr = configCreatedAt.toLocalDateTime().format(formatter);
    }
    public void setCreatedStrDetail() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분");
        createdStr = configCreatedAt.toLocalDateTime().format(formatter);
    }

}
