package com.starryNougat.PokemonGo.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.starryNougat.PokemonGo.dto.PMReleaseDates;
import com.starryNougat.PokemonGo.models.GlobalPokemonName;
import com.starryNougat.PokemonGo.models.QGlobalPokemonName;
import jakarta.persistence.EntityManager;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CalendarService {

    @Autowired
    EntityManager em;

    public List<PMReleaseDates> getDates() throws IOException {
        List<GlobalPokemonName> pmList = getEnNames();
        List<PMReleaseDates> dateList = new ArrayList<>();
        List<String> enNames = pmList.stream().map(dto -> dto.getPmNm()).toList();

        enNames.forEach(name->{
            try {
                dateList.addAll(getOnePokemon(name));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return dateList;
    }

    public List<PMReleaseDates> getOnePokemon(String pokemonName) throws IOException {
        try {
            ArrayList<PMReleaseDates> onePokemonInfos = new ArrayList<>();
            pokemonName = pokemonName.replaceAll("([^-])([A-Z])", "$1_$2");

            String url = "https://pokemongo.fandom.com/wiki/" + URLEncoder.encode(pokemonName, "UTF-8") + "?so=search";
            Document doc = Jsoup.connect(url).timeout(30*1000).get();
            int pokedexNum = Integer.parseInt(doc.select(".pogo-nav > div:nth-child(2) > .n1").first().text().replace("#",""));

            Elements formsHeader = doc.select("#Forms");
            Element availHeader = doc.select("span#Availability.mw-headline").first().parent();

            /******** 정보 입력 우선순위 ( h3 > form > 일반 ) ********/
            if(availHeader.nextElementSibling().tagName().equals("ul") || availHeader.nextElementSibling().tagName().equals("figure")) {
                //소제목(h3)이 없다
                if(formsHeader.size() == 0){ //폼 정보가 입력되지 않았을 경우에만 일반 포켓몬 입력
                    //바로 해당 ul 의 날짜 정보 읽어서 입력하기
                    //지역몬(모습이 다른)의 경우 지도(figure 태그) 태그xN개 건너뛰고 시작하기
                    Elements liList = availHeader.nextElementSiblings().not("figure").first().children();
                    onePokemonInfos.add(setReleaseDate(new PMReleaseDates(pokemonName, pokedexNum), liList));
                } else {
                    Elements formTags = doc.select(".wds-is-current .pogo-list-item-form");
                    List<String> forms = formTags.stream().map(form -> form.text()).toList();
                    Elements ul = doc.select("span#Availability.mw-headline").first().parent().nextElementSibling().children();
                    forms.forEach(form -> {
                        onePokemonInfos.add(setReleaseDate(new PMReleaseDates(form, pokedexNum, "----","----","----"), ul));
                    });
                }
            } else {
                //소제목(h3)이 있다 = 1개 이상의 정보를 읽어야 함
                Element iteratingHeader = availHeader.nextElementSibling();
                while (true) {
                    if(iteratingHeader.nextElementSibling().tagName().equals("ul")) {
                        //해당 ul 의 날짜 정보 읽어서 입력하기
                        Elements liList = iteratingHeader.nextElementSibling().children();
                        onePokemonInfos.add(setReleaseDate(new PMReleaseDates(iteratingHeader.child(0).text(), pokedexNum), liList));
                    } else if(iteratingHeader.nextElementSibling().tagName().equals("h2")){
                        //다음 제목(h2)가 오기 전까지 반복해서 Availability 내용 모두 분석
                        break;
                    }
                    //다음 sibling 읽기
                    iteratingHeader = iteratingHeader.nextElementSibling();
                }
            }
            System.out.println(pokemonName);
            return onePokemonInfos;

        } catch(HttpStatusException e) {
            //오류 날 경우 console에 오류 출력 및 오류데이터 반환 반환
            System.out.println(String.format("[ERROR] pmName:%s, errorCode:%s, message:%s",pokemonName,e.getStatusCode(),e.getLocalizedMessage()));
            return List.of(new PMReleaseDates(pokemonName, "HttpStatusCode:"+Integer.toString(e.getStatusCode()), "----", "----"));
        }
    }
    public PMReleaseDates setReleaseDate(PMReleaseDates pmReleaseDates, Elements liList) {
        return setReleaseDate(pmReleaseDates, liList, false);
    }
    public PMReleaseDates setReleaseDate(PMReleaseDates pmReleaseDates, Elements liList, boolean hasForms) {
        try {
            for (int i = 0; i < liList.size(); i++) {
                Element li = liList.get(i);
                Elements aTags = li.select("a");

                if (aTags.size() > 0) { //a 태그가 존재하는 li 태그에 한해서 날짜 입력 작업 실행
                    Elements liListForTesting = li.parent().select("li:not(:has(ul))");
                    String date = changeDateFormat(li.parent().select("li:nth-child(" + (i + 1) + ") > a:last-of-type").first().text());

                    if (aTags.text().contains("Shiny form")) { //이로치 정보
                        if (date == null && li.text().contains("on the same day")) {
                            //이로치 정보가 있지만 date 정보가 없을 경우 on the same day 로 표기되어있음 -> 릴리스 날짜와 일치함
                            date = pmReleaseDates.getDt();
                        }
                        pmReleaseDates.setShinyDt(date);
                    } else if (aTags.text().contains("Shadow form")) { //그림자 정보
                        //그림자 이로치 정보도 같이 있는 경우 제외하기 위해 select("a:last-of-type").first() 로 제어
                        pmReleaseDates.setShadowDt(date);
                    } else { //일반 릴리스 정보
                        if (pmReleaseDates.getDt() != null /*일반 미입력칸*/ && pmReleaseDates.getDt() != "----" /*form의 미입력칸*/)
                            continue; //이미 릴리스 정보가 있다면 다시 지정하지 않음
                        pmReleaseDates.setDt(date);
                    }
                }
                ;
            }
            return pmReleaseDates;
        } catch (Exception e) {
            System.out.println("[Error] pmName: "+pmReleaseDates.getPokemonName()+", pokedexNum: "+pmReleaseDates.getPokedexNum());
            e.printStackTrace();
            return pmReleaseDates;
        }
    }

    public String changeDateFormat(String dateText){
        //day에 붙은 ordinal indicator 제거
        dateText = dateText.replaceAll("(?<=\\d)(st|nd|rd|th)", "");

        try{
            DateTimeFormatter originDateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateText, originDateFormat).format(newDateFormat);
        } catch (DateTimeParseException e) {
            //date 형식이 아닐 경우 null 반환
            return null;
        }
    }

    public List<GlobalPokemonName> getEnNames(){
        JPAQueryFactory jq = new JPAQueryFactory(em);
        QGlobalPokemonName pmName = new QGlobalPokemonName("pmName");

        return jq.select(Projections.constructor(GlobalPokemonName.class,
                pmName.languageSeq,
                pmName.pmPokedexNum,
                pmName.regionSeq,
                pmName.pmForm,
                pmName.pmNm
        )).from(pmName)
                .where(pmName.languageSeq.eq(2)) /*영어이름 가져오기*/
                .fetch();
    }
}


