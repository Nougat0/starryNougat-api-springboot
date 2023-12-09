package com.starryNougat.PokemonGo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name="event_type")
public class EventType {
    @Id
    @Column(name="EVENT_TYPE_SEQ")
    private int eventTypeSeq;

    @Column(name="EVENT_TYPE_MST")
    private String eventTypeMst;

    @Column(name="EVENT_TYPE_DTL")
    private String eventTypeDtl;

    @Column(name="EVENT_TYPE_NM")
    private String eventTypeNm;

    @Column(name="USED_YN")
    private String usedYn;

    public EventType() {
    }

    public EventType(int eventTypeSeq, String eventTypeMst, String eventTypeDtl, String eventTypeNm, String usedYn) {
        this.eventTypeSeq = eventTypeSeq;
        this.eventTypeMst = eventTypeMst;
        this.eventTypeDtl = eventTypeDtl;
        this.eventTypeNm = eventTypeNm;
        this.usedYn = usedYn;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "eventTypeSeq=" + eventTypeSeq +
                ", eventTypeMst='" + eventTypeMst + '\'' +
                ", eventTypeDtl='" + eventTypeDtl + '\'' +
                ", eventTypeNm='" + eventTypeNm + '\'' +
                ", usedYn='" + usedYn + '\'' +
                '}';
    }

    public int getEventTypeSeq() {
        return eventTypeSeq;
    }

    public void setEventTypeSeq(int eventTypeSeq) {
        this.eventTypeSeq = eventTypeSeq;
    }

    public String getEventTypeMst() {
        return eventTypeMst;
    }

    public void setEventTypeMst(String eventTypeMst) {
        this.eventTypeMst = eventTypeMst;
    }

    public String getEventTypeDtl() {
        return eventTypeDtl;
    }

    public void setEventTypeDtl(String eventTypeDtl) {
        this.eventTypeDtl = eventTypeDtl;
    }

    public String getEventTypeNm() {
        return eventTypeNm;
    }

    public void setEventTypeNm(String eventTypeNm) {
        this.eventTypeNm = eventTypeNm;
    }

    public String getUsedYn() {
        return usedYn;
    }

    public void setUsedYn(String usedYn) {
        this.usedYn = usedYn;
    }
}
