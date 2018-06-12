package com.flowertale.flowertaleandroid.entity;

public class InvitationRecord {
    private Integer inviteId;
    private String inviter;
    private String groupName;

    public String getInviter() {

        return inviter;
    }

    public String getGroupName() {
        return groupName;
    }

    public InvitationRecord(Integer inviteId, String inviter, String groupName) {
        this.inviteId = inviteId;
        this.inviter = inviter;
        this.groupName = groupName;
    }

    public Integer getInviteId() {
        return inviteId;
    }
}
