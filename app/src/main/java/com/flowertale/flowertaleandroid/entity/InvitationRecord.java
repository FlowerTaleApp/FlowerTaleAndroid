package com.flowertale.flowertaleandroid.entity;

public class InvitationRecord {
    private String inviter;
    private String groupName;

    public InvitationRecord(String inviter, String groupName) {
        this.inviter = inviter;
        this.groupName = groupName;
    }

    public String getInviter() {

        return inviter;
    }

    public String getGroupName() {
        return groupName;
    }
}
