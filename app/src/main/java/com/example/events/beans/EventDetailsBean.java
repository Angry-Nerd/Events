package com.example.events.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class EventDetailsBean implements Parcelable {
    private String eventName;
    private String eventOrganiser;
    private String eventVenue;
    private String eventDescription;
    private String eventImageURL;
    private String eventRegisterationLink;
    private String eventsEmailOfOrganizer;

    public EventDetailsBean(){}

    public EventDetailsBean(String eventName, String eventOrganiser, String eventVenue, String eventDescription, String eventImageURL, String eventRegisterationLink, String eventsEmailOfOrganizer, String dateOfEvent) {
        this.eventName = eventName;
        this.eventOrganiser = eventOrganiser;
        this.eventVenue = eventVenue;
        this.eventDescription = eventDescription;
        this.eventImageURL = eventImageURL;
        this.eventRegisterationLink = eventRegisterationLink;
        this.eventsEmailOfOrganizer = eventsEmailOfOrganizer;
        this.dateOfEvent = dateOfEvent;
    }

    public String getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(String dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    private String dateOfEvent;

    protected EventDetailsBean(Parcel in) {
        eventName = in.readString();
        eventOrganiser = in.readString();
        eventVenue = in.readString();
        eventDescription = in.readString();
        eventImageURL = in.readString();
        eventRegisterationLink = in.readString();
        eventsEmailOfOrganizer = in.readString();
    }

    public static final Creator<EventDetailsBean> CREATOR = new Creator<EventDetailsBean>() {
        @Override
        public EventDetailsBean createFromParcel(Parcel in) {
            return new EventDetailsBean(in);
        }

        @Override
        public EventDetailsBean[] newArray(int size) {
            return new EventDetailsBean[size];
        }
    };

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventOrganiser() {
        return eventOrganiser;
    }

    public void setEventOrganiser(String eventOrganiser) {
        this.eventOrganiser = eventOrganiser;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImageURL() {
        return eventImageURL;
    }

    public void setEventImageURL(String eventImageURL) {
        this.eventImageURL = eventImageURL;
    }

    public String getEventRegisterationLink() {
        return eventRegisterationLink;
    }

    public void setEventRegisterationLink(String eventRegisterationLink) {
        this.eventRegisterationLink = eventRegisterationLink;
    }

    public String getEventsEmailOfOrganizer() {
        return eventsEmailOfOrganizer;
    }

    public void setEventsEmailOfOrganizer(String eventsEmailOfOrganizer) {
        this.eventsEmailOfOrganizer = eventsEmailOfOrganizer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventName);
        parcel.writeString(eventOrganiser);
        parcel.writeString(eventVenue);
        parcel.writeString(eventDescription);
        parcel.writeString(eventImageURL);
        parcel.writeString(eventRegisterationLink);
        parcel.writeString(eventsEmailOfOrganizer);
    }
}
