package com.lotteon.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetConfigListDTO {
    private Long id;

    private Timestamp configCreatedAt;

    private boolean configIsUsed;

    private String createdStr;


    public void setCreatedStr() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
        createdStr = configCreatedAt.toLocalDateTime().format(formatter);

    }
}
