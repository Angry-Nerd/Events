package com.example.events.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class PastCard implements Parcelable {
    private String cardImageURL, cardTitle, cardDescription, dateOfEvent, cardOrganizer;

    public PastCard() {
    }

    public PastCard(String cardImageURL, String cardTitle, String cardDescription, String dateOfEvent, String cardOrganizer) {
        this.cardImageURL = cardImageURL;
        this.cardTitle = cardTitle;
        this.cardDescription = cardDescription;
        this.dateOfEvent = dateOfEvent;
        this.cardOrganizer = cardOrganizer;
    }

    public String getCardOrganizer() {
        return cardOrganizer;
    }

    public void setCardOrganizer(String cardOrganizer) {
        this.cardOrganizer = cardOrganizer;
    }

    protected PastCard(Parcel in) {
        cardImageURL = in.readString();
        cardTitle = in.readString();
        cardDescription = in.readString();
        dateOfEvent = in.readString();
    }

    public static final Creator<PastCard> CREATOR = new Creator<PastCard>() {
        @Override
        public PastCard createFromParcel(Parcel in) {
            return new PastCard(in);
        }

        @Override
        public PastCard[] newArray(int size) {
            return new PastCard[size];
        }
    };

    public String getCardImageURL() {
        return cardImageURL;
    }

    public void setCardImageURL(String cardImageURL) {
        this.cardImageURL = cardImageURL;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(String dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cardImageURL);
        parcel.writeString(cardTitle);
        parcel.writeString(cardDescription);
        parcel.writeString(dateOfEvent);
    }
}
