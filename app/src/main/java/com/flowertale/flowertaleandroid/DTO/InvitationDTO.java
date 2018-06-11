package com.flowertale.flowertaleandroid.DTO;

import lombok.Data;

@Data
public class InvitationDTO {

    private Integer id;
    private Integer teamId;
    private String teamName;
    private Integer inviterId;
    private String inviterName;

    public InvitationDTO(Integer id, Integer teamId, String teamName, Integer inviterId, String inviterName) {
        this.id = id;
        this.teamId = teamId;
        this.teamName = teamName;
        this.inviterId = inviterId;
        this.inviterName = inviterName;
    }
}
