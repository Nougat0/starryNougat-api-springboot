package com.starryNougat.PokemonGo.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.starryNougat.PokemonGo.dto.EventInfo;
import com.starryNougat.PokemonGo.dto.PMReleaseDates;
import com.starryNougat.PokemonGo.dto.PMTypes;
import com.starryNougat.PokemonGo.models.*;
import com.starryNougat.PokemonGo.repository.PokemonRepository;
import jakarta.persistence.EntityManager;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class EventService {

    final String FANDOM_BASE_URL = "https://pokemongo.fandom.com/wiki/";
    final String OFFICIAL_BASE_URL = "https://pokemongolive.com/post/";
    @Autowired
    EntityManager em;

    /**
     * 한 해의 모든 이벤트 목록 가져오기
     * @param year
     * @return
     * 
     * 진행순서
     * [1] 이벤트 이름 목록 가져오기 List<String>
     *     [a] 시즌이 있을 경우 시즌 상세페이지 들어가서 이벤트 목록 가져오기
     *     [b] 시즌이 없을 경우 이벤트 목록 가져오기
     *     ※ a와 b는 if~else로 묶이지 않는다
     * [2] 이벤트 이름으로 url 만들기
     *     [a] url 로 이벤트 상세페이지 들어가기
     *     [b] 상세페이지에서 필요한 정보 가져오기
     */
    public List<Event> getEventListofYear(int year) throws IOException {
        List<EventInfo> yearEvents = new ArrayList<>();

        // [1] 한 해의 모든 이벤트 목록 가져오기
        List<String> eventNameList = getEventNames(year);

        // [2] 이벤트 이름으로 url 만들기
        for (String eventName : eventNameList) {

            List<EventBonusJoin> bonusList = new ArrayList<>();
            List<EventPokemonJoin> pokemonList = new ArrayList<>();
            Event event = new Event(eventName);

            String url = FANDOM_BASE_URL + eventName.replaceAll(" ", "_");
            Document doc = Jsoup.connect(url).timeout(30*1000).get();
            //이벤트 페이지에서 이벤트 정보 가져오기

            EventInfo info = new EventInfo(event, pokemonList, bonusList);
            yearEvents.add(info);
        }

        //     [a] url 로 이벤트 상세페이지 들어가기
        //     [b] 상세페이지에서 필요한 정보 가져오기
        
        return null;
    }

    /**
     * 한 해의 모든 이벤트 목록 가져오기
     * @param year
     * @return
     * @throws IOException
     */
    public List<String> getEventNames(int year) throws IOException {
        String url = FANDOM_BASE_URL + "List_of_Events";
        Document doc = Jsoup.connect(url).timeout(30*1000).get();

        List<String> seasonNameList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();

        // [*] 시즌 있는지 체크하기

        // [b] 일반 이벤트 정보 가져오기
        Elements yearEvents = doc.selectFirst("#"+year).parent().nextElementSibling().select("div.pogo-list-item > a");
        yearEvents.forEach(event -> eventNameList.add(event.text()));

        // [a] 시즌 이벤트 정보 가져오기
        Element yearList = doc.selectXpath("//*[@id=\"toc\"]/ul").first();
        Element seasonUlTag = yearList.selectXpath("//a[@href=\"#"+year+"\"]").first().nextElementSiblings().first();
        if(seasonUlTag != null) {
            //시즌에 따른 이벤트 추가
            Elements seasonTags = seasonUlTag.select(".toctext");
            for(Element season : seasonTags) {
                //시즌 정보 추가하기
                seasonNameList.add(season.text());
                String seasonName = season.text().replace(" ", "_");
                String tagName = "#List_of_" + seasonName + "_Events";

                Document seasonDoc = Jsoup.connect(FANDOM_BASE_URL + seasonName + tagName).timeout(30*1000).get();
                Elements seasonEvents = seasonDoc.selectFirst(tagName.replace(":", "\\:")).parent().nextElementSibling().select("div.pogo-list-item > a");
                seasonEvents.forEach(event -> eventNameList.add(event.attr("title")));
            }
        }
        return eventNameList;
    }
}


