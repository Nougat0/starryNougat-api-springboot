package com.starryNougat.PokemonGo.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="event")
public class Event {
    @Id
    @Column(name="EVENT_SEQ")
    private int eventSeq;

    @Column(name="EVENT_TYPE_SEQ")
    private int eventTypeSeq;

    @Column(name="EVENT_NM")
    private String eventNm;

    @Column(name="EVENT_START")
    private Timestamp eventStart;

    @Column(name="EVENT_END")
    private Timestamp eventEnd;

    public Event() {
    }

    public Event(String eventNm) {
        this.eventNm = eventNm;
    }

    public Event(String eventNm, Timestamp eventStart, Timestamp eventEnd) {
        this.eventNm = eventNm;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }
    public Event(int eventTypeSeq, String eventNm, Timestamp eventStart, Timestamp eventEnd) {
        this.eventTypeSeq = eventTypeSeq;
        this.eventNm = eventNm;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    public Event(int eventSeq, int eventTypeSeq, String eventNm, Timestamp eventStart, Timestamp eventEnd) {
        this.eventSeq = eventSeq;
        this.eventTypeSeq = eventTypeSeq;
        this.eventNm = eventNm;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventSeq=" + eventSeq +
                ", eventTypeSeq=" + eventTypeSeq +
                ", eventNm='" + eventNm + '\'' +
                ", eventStart=" + eventStart +
                ", eventEnd=" + eventEnd +
                '}';
    }

    public int getEventSeq() {
        return eventSeq;
    }

    public void setEventSeq(int eventSeq) {
        this.eventSeq = eventSeq;
    }

    public int getEventTypeSeq() {
        return eventTypeSeq;
    }

    public void setEventTypeSeq(int eventTypeSeq) {
        this.eventTypeSeq = eventTypeSeq;
    }

    public String getEventNm() {
        return eventNm;
    }

    public void setEventNm(String eventNm) {
        this.eventNm = eventNm;
    }

    public Timestamp getEventStart() {
        return eventStart;
    }

    public void setEventStart(Timestamp eventStart) {
        this.eventStart = eventStart;
    }

    public Timestamp getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Timestamp eventEnd) {
        this.eventEnd = eventEnd;
    }
}
