package com.usta.startupconnect.dto;

public class MeetingResponse {
    private String eventId;
    private String meetingLink;
    private String message;
    private String summary;
    private String startTime;
    private String endTime;

    public MeetingResponse(String eventId, String meetingLink, String message, 
                          String summary, String startTime, String endTime) {
        this.eventId = eventId;
        this.meetingLink = meetingLink;
        this.message = message;
        this.summary = summary;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}