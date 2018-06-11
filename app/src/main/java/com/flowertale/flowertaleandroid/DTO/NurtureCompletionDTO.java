package com.flowertale.flowertaleandroid.DTO;

import java.util.List;

import lombok.Data;

@Data
public class NurtureCompletionDTO {

    private Integer plantId;
    private String plantName;
    private Integer teamId;
    private String teamName;
    private List<CompletionItemDTO> completionItemDTOList;
}
