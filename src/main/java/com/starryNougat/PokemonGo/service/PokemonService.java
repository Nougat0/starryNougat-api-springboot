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
public class PokemonService {

    final String FANDOM_BASE_URL = "https://pokemongo.fandom.com/wiki/";
    final String OFFICIAL_BASE_URL = "https://pokemongolive.com/post/";
    @Autowired
    EntityManager em;

    public enum formType {
        Mega,
        Alolan,
        Hisuian,
        Galarian
    }

    final String Mega = "메가";
    final String Alolan = "알로라";
    final String Hisuian = "히스이";
    final String Galarian = "가라르";

    @Autowired
    PokemonRepository repo;

    public List<Pokemon> getDates() throws IOException {
        List<GlobalPokemonName> pmList = getEnNames();
        List<Pokemon> dateList = new ArrayList<>();
        List<String> enNames = pmList.stream().map(dto -> dto.getPmNm()).toList();

        for (String pokemon: enNames) {
            dateList.addAll(getOnePokemon(pokemon));
            List<Pokemon> onePokedexNum = getOnePokemon(pokemon);
        }
        return dateList;
    }

    /**
     * 포켓몬 1종류에 대한 정보 가져오기
     * 릴리스 정보, Type 등을 form 종류별로 가져오기
     * @param pokemonName
     * @return
     * @throws IOException
     */
    public List<Pokemon> getOnePokemon(String pokemonName) throws IOException {
        //페이지 연결
        //페이지에서 릴리스 날짜 가져오기
        //페이지에서 폼 종류 가져오기
        List<Pokemon> onePokemonInfo = new ArrayList<>();

        try {
            ArrayList<PMReleaseDates> onePokemonInfos = new ArrayList<>();
//            pokemonName = pokemonName.replaceAll("([^-])([A-Z])", "$1_$2");

            String url = FANDOM_BASE_URL + URLEncoder.encode(pokemonName.replaceAll("([^-])([A-Z])", "$1_$2"), "UTF-8") + "?so=search";
            Document doc = Jsoup.connect(url).timeout(30*1000).get();
            int pokedexNum = Integer.parseInt(doc.select(".pogo-nav > div:nth-child(2) > .n1").first().text().replace("#",""));

//            List<PMTypes> types = getPokemonType(doc, pokedexNum);

            Elements formsHeader = doc.select("#Forms");
            Element availHeader = doc.select("span#Availability.mw-headline").first().parent();

            /******** 정보 입력 우선순위 ( 소제목[h3] > form > 일반 ) ********/
            if(availHeader.nextElementSibling().tagName().equals("ul") || availHeader.nextElementSibling().tagName().equals("figure")) {
                //소제목(h3)이 없다
                if(formsHeader.size() == 0 || formsHeader.first().parent().nextElementSibling().tagName().equals("p")){ //폼 정보가 입력되지 않았을 경우 일반 포켓몬 입력
                    //바로 해당 ul 의 날짜 정보 읽어서 입력하기
                    //지역몬(모습이 다른)의 경우 지도(figure 태그) 태그xN개 건너뛰고 시작하기
                    Elements liList = availHeader.nextElementSiblings().not("figure").first().children();
                    onePokemonInfos.add(setReleaseDate(new PMReleaseDates("Normal", pokedexNum), liList));
                } else {
                    Elements formTags = doc.select(".wds-is-current .pogo-list-item-form");
                    List<String> forms = formTags.stream().map(form -> form.text()).toList();
                    Elements ul = doc.select("span#Availability.mw-headline").first().parent().nextElementSibling().children();
                    forms.forEach(form -> {
                        onePokemonInfos.add(setReleaseDate(new PMReleaseDates(form, pokedexNum), ul));
                    });
                }
            } else {
                //소제목(h3)이 있다 = 1개 이상의 정보를 읽어야 함
                Element iteratingHeader = availHeader.nextElementSibling();
                while (true) {
                    if(iteratingHeader.nextElementSibling().tagName().equals("ul")) {
                        //해당 ul 의 날짜 정보 읽어서 입력하기
                        Elements liList = iteratingHeader.nextElementSibling().children();

                        String pokemonNm = "";
                        String formName = iteratingHeader.selectFirst("span.mw-headline").text();
                        if(formName.equalsIgnoreCase(pokemonName)) pokemonNm = "Normal";
                        else pokemonNm = formName;

                        onePokemonInfos.add(setReleaseDate(new PMReleaseDates(pokemonNm, pokedexNum), liList));
                    } else if(iteratingHeader.nextElementSibling().tagName().equals("h2")){
                        //다음 제목(h2)가 오기 전까지 반복해서 Availability 내용 모두 분석
                        break;
                    }
                    //다음 sibling 읽기
                    iteratingHeader = iteratingHeader.nextElementSibling();
                }
            }

            //포켓몬 폼별 종류 가져오기
            List<PMTypes> pmTypes = getPokemonType(doc, pokedexNum);

            if(pmTypes.size() > onePokemonInfos.size()) onePokemonInfos.add(new PMReleaseDates());

            //PMReleaseDates에 type 정보 채워넣기
            for(PMReleaseDates pmReleaseDate : onePokemonInfos) {
                PMTypes pmType = pmTypes.stream()
//                        .filter(form -> pmReleaseDate.getPokemonName().contains(form.getFormName()))
                        .filter(form -> Arrays.stream(form.getFormName().split(" ")).anyMatch(pmReleaseDate.getPokemonName()::contains))
                        .toList().get(0);
                //items.stream().anyMatch(inputStr::contains)
                pmReleaseDate.setType1(pmType.getType1());
                pmReleaseDate.setType2(pmType.getType2());
            }

            return insertPokemon(onePokemonInfos);

        } catch(HttpStatusException e) {
            //오류 날 경우 console에 오류 출력 및 오류데이터 반환 반환
            System.out.println(String.format("[HttpStatusException ERROR] pmName:%s, errorCode:%s, message:%s",pokemonName,e.getStatusCode(),e.getLocalizedMessage()));
            return null;
        } catch(ParseException e) {
            System.out.println(String.format("[ParseException ERROR] pmName:%s, errorCode:%s, message:%s",pokemonName,e.getLocalizedMessage()));
            return null;
        }
    }

    /**
     * 포켓몬 1종류에 대한 정보 DB에 insert시키는 로직
     * @param onePokemonInfos
     * @return
     * @throws ParseException
     */
    private List<Pokemon> insertPokemon(List<PMReleaseDates> onePokemonInfos) throws ParseException {
        JPAQueryFactory jq = new JPAQueryFactory(em);
        QPokemon pm = new QPokemon("pm");
        QGlobalPokemonName gpm = new QGlobalPokemonName("gpm");

        //DB에 넣을 값 담을 바구니 생성
        List<Pokemon> pokemons = new ArrayList<>();
        PMReleaseDates firstData = onePokemonInfos.get(0);
        int regionNum = 11;
        Pokemon pokemon = jq.selectFrom(pm).where(pm.pmPokedexNum.eq(firstData.getPokedexNum())).fetchFirst();
        regionNum = pokemon.getRegionSeq();
        String type1 = null, type2 = null, formName = "일반";

        //검색 시 사용했던 이름 가져오기
        String pokemonDefaultName = jq
                .select(gpm.pmNm)
                .from(gpm)
                .innerJoin(pm).on(pm.pmPokedexNum.eq(gpm.pmPokedexNum))
                .where(gpm.languageSeq.eq(2).and(gpm.pmPokedexNum.eq(firstData.getPokedexNum())))
                .fetchFirst();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        //폼 종류별로 다르게 지정하여 저장하기
        for(PMReleaseDates oneForm : onePokemonInfos) {
            String pokemonFormName = oneForm.getPokemonName();

            //폼 종류 중 리전폼, 메가진화 걸러내기
            switch(pokemonFormName.split(" ")[0]) {
                case "Mega":
                    formName = "메가";
                    break;
                case "Alolan":
                    formName = "알로라";
                    regionNum = 7;
                    break;
                case "Galarian":
                    formName = "가라르";
                    regionNum = 8;
                    break;
                case "Hisuian":
                    formName = "히스이";
                    regionNum = 9;
                    break;
                default:
                    // 타입 채워넣기
                    if(oneForm.getType1() == null && oneForm.getType2() == null){
                        type1 = pokemon.getPmType1();
                        type2 = pokemon.getPmType2();
                    } else { //타입이 하나라도 null이 아닐 경우에는
                        type1 = oneForm.getType1();
                        type2 = oneForm.getType2();
                    }

                    if(pokemonDefaultName.replaceAll("([^-])([A-Z])", "$1_$2").equals(pokemonFormName))
                        break; //case에는 상수만 넣을 수 있음
                    else formName = pokemonFormName; //폼 이름 넣기
            }

            Pokemon insertPM = new Pokemon(oneForm.getPokedexNum(), regionNum, formName,null, type1, type2);

            try {
                insertPM.setPmReleaseDt(formatter.parse(oneForm.getDt()));
                insertPM.setShinyReleaseDt(formatter.parse(oneForm.getShinyDt()));
                insertPM.setShadowReleaseDt(formatter.parse(oneForm.getShadowDt()));
            } catch(ParseException e) {
                System.out.println("날짜 파싱 오류: ---- 반환됨" + insertPM); //셋 중 하나라도 없으면 오류 발생 (null 값 등)
            } catch(NullPointerException e) {
                System.out.println("날짜 없음 오류:"+insertPM);
            }
            pokemons.add(insertPM);
        }

        return repo.saveAll(pokemons);
    }


    /**
     * 포켓몬 릴리스 날짜 긁어서 받은 PMReleaseDates 에 넣어서 반환하기
     * @param pmReleaseDates
     * @param liList
     * @return
     */
    public PMReleaseDates setReleaseDate(PMReleaseDates pmReleaseDates, Elements liList) {
        return setReleaseDate(pmReleaseDates, liList, false);
    }

    /**
     * 포켓몬 릴리스 날짜 긁어서 받은 PMReleaseDates에 넣어서 반환하기
     * @param pmReleaseDates
     * @param liList
     * @return
     */
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

    /**
     * 날짜 형식 변환하기 
     * 웹페이지에서 갖고 있는 날짜형식을 yyyy-MM-dd 로 변경하기
     * @param dateText
     * @return
     */
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

    /**
     * 모든 포켓몬의 영어 이름 가져오기
     * @return
     */
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
                .groupBy(pmName.pmPokedexNum)
                .fetch();
    }

    /**
     * 포켓몬 타입정보 가져오기
     * @param doc
     * @param pokedexNum
     * @return
     */
    public List<PMTypes> getPokemonType (Document doc, int pokedexNum) {
        List<PMTypes> pmTypes = new ArrayList<>();
        List<String> formNames = new ArrayList<>(doc
                .select(".poketab.multiinfobox > .tabber > .wds-tabs__wrapper > .wds-tabs > li")
                .stream().map(element -> element.attr("data-hash").replace("_", " ")).toList());
        Elements formTags = doc.select(".poketab.multiinfobox > .tabber > .wds-tab__content");
        if(formTags.size() == 0 && formNames.size() == 0) {
            //폼이 분리되어있지 않을 경우, 기본폼 찾아서 사용하기
            formTags.add(doc.selectFirst(".mw-parser-output > aside"));
            formNames.add(new String("Normal"));
        }
        for (int idx = 0; idx < formTags.size(); idx++) {
            Element form = formTags.get(idx);
            String formName = formNames.get(idx).replace("Regular", "Normal"); /*혹시나 Regular를 사용한다면 Normal 로 변경해주기*/
            String type1 = form.selectFirst("div[data-source=\"type1\"]").selectFirst("a:nth-child(2)").text();
            String type2 = form.selectFirst("div[data-source=\"type2\"]") == null ? null : form.selectFirst("div[data-source=\"type2\"]").selectFirst("a:nth-child(2)").text();
            pmTypes.add(new PMTypes(pokedexNum, formName, type1, type2));
        }
        return pmTypes;
    }

    /**
     * 아직 웹페이지에서 업로드되지 않은 포켓몬의 번호 체크하기
     * @param siteName
     * @return
     * @throws IOException
     */
    public List<Integer> getMissing (String siteName) throws IOException {
        List<Pokemon> pokemonList = new ArrayList<>();
        switch(siteName) { /*siteName에 따라 실행함수 구분가능*/
            case "fandom":
                pokemonList = getDates();
                break;
        }
        List<Integer> missingList = new ArrayList<>();
        int size = pokemonList.size();
        for(int idx=0; idx<size-1; idx++) {
            int thisPokedexNum = pokemonList.get(idx).getPmPokedexNum();
            int nextPokedexNum = pokemonList.get(idx+1).getPmPokedexNum();
            if(nextPokedexNum - thisPokedexNum > 1) {
                for(int i=thisPokedexNum + 1; i<nextPokedexNum; i++) {
                    missingList.add(i);
                }
            }
        }
        return missingList;
    }

    /**
     * 한글화된 폼 정보 가져오기
     * @param pokemonName
     * @return
     * @throws IOException
     */
    public HashMap getOtherForms(String pokemonName) throws IOException {
        String url = "https://namu.wiki/w/%EB%AA%A8%EC%8A%B5%EC%9D%B4%20%EB%8B%A4%EB%A5%B8%20%ED%8F%AC%EC%BC%93%EB%AA%AC#s-3";
        Document doc = Jsoup.connect(url).timeout(30*1000).get();
        Elements elements = doc.select(".cIflhYhI");


        HashMap<String, List<String>> otherForms = new HashMap<>();



        return null;
    }
}


