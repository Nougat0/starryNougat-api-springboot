package com.starryNougat.PokemonGo.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class PokemonService {

    final String FANDOM_BASE_URL = "https://pokemongo.fandom.com/wiki/";
    final String OFFICIAL_BASE_URL = "https://pokemongolive.com/post/";
    @Autowired
    EntityManager em;

    public enum Region {
        Region("지역명"), //ordinal 함수 사용 시 정확한 숫자가 표현되도록 넣은 값
        Kanto("관동"), //ordinal = 1
        Johto("성도"), //ordinal = 2
        Hoenn("호연"), //ordinal = 3
        Sinnoh("신오"), //ordinal = 4
        Unova("하나"), //ordinal = 5
        Kalos("칼로스"), //ordinal = 6
        Alola("알로라"), //ordinal = 7
        Galar("가라르"), //ordinal = 8
        Hisui("히스이"), //ordinal = 9
        Paldea("팔데아"), //ordinal = 10
        Unknown("미분류"); //ordinal = 11

        private final String key;

        // 생성자 정의
        Region(String key) {
            this.key = key;
        }

        // 추가 메소드 정의
        public String getKey() {
            return key;
        }

        public static int getValueByEng(String regionName) {
            for(Region region: Region.values()) {
                if(regionName.equalsIgnoreCase(String.valueOf(region)))
                    return region.ordinal();
            }
            return -1;
        }
    }

    @Autowired
    PokemonRepository repo;

    public List<Pokemon> getDates() throws IOException {
        List<GlobalPokemonName> pmList = getEnNames();
        List<Pokemon> dateList = new ArrayList<>();
        List<String> enNames = pmList.stream().map(dto -> dto.getPmNm()).toList();

        for (String pokemon: enNames) dateList.addAll(getOnePokemon(pokemon));

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
        System.out.println(pokemonName);
        //페이지 연결
        //페이지에서 릴리스 날짜 가져오기
        //페이지에서 폼 종류 가져오기
        List<Pokemon> onePokemonInfo = new ArrayList<>();

        //한글로 검색했을 때도 서칭 가능하도록 하기
        if(Pattern.matches("^[가-힣]*$", pokemonName)) {
            JPAQueryFactory jq = new JPAQueryFactory(em);
            QGlobalPokemonName gpm = new QGlobalPokemonName("gpm");
            int pokedexNum = jq.select(gpm.pmPokedexNum).where(gpm.pmNm.eq(pokemonName)).fetchFirst();
            String englishName = jq.select(gpm.pmNm).where(gpm.pmPokedexNum.eq(pokedexNum).and(gpm.languageSeq.eq(2))).fetchFirst();
            if(englishName != null) pokemonName = englishName;
        }

        try {
            List<Pokemon> onePokemonInfos = new ArrayList<>();
//            pokemonName = pokemonName.replaceAll("([^-])([A-Z])", "$1_$2");

            String url = FANDOM_BASE_URL + URLEncoder.encode(pokemonName.replaceAll("([^-])([A-Z])", "$1_$2"), "UTF-8") + "?so=search";
            Document doc = Jsoup.connect(url).timeout(30*1000).get();
            int pokedexNum = Integer.parseInt(doc.select(".pogo-nav > div:nth-child(2) > .n1").first().text().replace("#",""));
            String region = doc.selectFirst("[title=\"Regions\"]").text();
            int regionSeq = Region.getValueByEng(region);

            Elements formsHeader = doc.select("#Forms");
            Element availHeader = doc.select("span#Availability.mw-headline").first().parent();

            /******** 정보 입력 우선순위 ( 소제목[h3] > form > 일반 ) ********/
            if(availHeader.nextElementSibling().tagName().equals("ul") || availHeader.nextElementSibling().tagName().equals("figure")) {
                //소제목(h3)이 없다
                if(formsHeader.size() == 0 || formsHeader.first().parent().nextElementSibling().tagName().equals("p")){ //폼 정보가 입력되지 않았을 경우 일반 포켓몬 입력
                    //바로 해당 ul 의 날짜 정보 읽어서 입력하기
                    //지역몬(모습이 다른)의 경우 지도(figure 태그) 태그xN개 건너뛰고 시작하기
                    Elements liList = availHeader.nextElementSiblings().not("figure").first().children();
                    onePokemonInfos.add(setReleaseDate(new Pokemon(pokedexNum, regionSeq,"Normal"), liList));
                } else {
                    Elements formTags = doc.select(".wds-is-current .pogo-list-item-form");
                    List<String> forms = formTags.stream().map(form -> form.text()).toList();
                    Elements ul = doc.select("span#Availability.mw-headline").first().parent().nextElementSibling().children();
                    forms.forEach(form -> {
                        onePokemonInfos.add(setReleaseDate(new Pokemon(pokedexNum, regionSeq, form), ul));
                    });
                }
            } else {
                //소제목(h3)이 있다 = 1개 이상의 정보를 읽어야 함
                Element iteratingHeader = availHeader.nextElementSibling();
                while (true) {
                    Element nextElement = iteratingHeader.nextElementSibling();
                    if(nextElement.tagName().equals("ul")) {
                        //해당 ul 의 날짜 정보 읽어서 입력하기
                        Elements liList = nextElement.children();

                        String reformedName = "";
                        String formName = "";
                        //지역몬일 경우 예외처리
                        if(iteratingHeader.tagName().equals("figure"))
                            formName = iteratingHeader.previousElementSibling().selectFirst("span.mw-headline").text();
                        else formName = iteratingHeader.selectFirst("span.mw-headline").text();

                        //찾는 포켓몬 이름과 동일한 formName일 경우 폼 종류는 Normal로 지정
                        if(formName.equalsIgnoreCase(pokemonName)) reformedName = "Normal";
                        else reformedName = formName.replaceAll("\\b("+pokemonName+"|Form|Forme)\\b", "").trim();

                        onePokemonInfos.add(setReleaseDate(new Pokemon(pokedexNum, regionSeq, reformedName), liList));
                    } else if(nextElement.tagName().equals("h2")){
                        //다음 제목(h2)가 오기 전까지 반복해서 Availability 내용 모두 분석
                        break;
                    }
                    //다음 sibling 읽기
                    iteratingHeader = nextElement;
                }
            }

            //포켓몬 폼별 종류 가져오기
            List<Pokemon> pmTypes = getPokemonType(doc, pokedexNum);
            //포켓몬 폼별 타입정보 맵핑하기 - index 순서가 똑같다는 가정 하에
            int pmTypeNo = pmTypes.size();
            int pmFormNo = onePokemonInfos.size();

            boolean goForTypeName = false;

            //폼 정보가 type 정보보다 많을 경우, type의 첫번째 요소를 차이나는 개수만큼 채워넣기
            if(pmFormNo > pmTypeNo) {
//                pmTypes.addAll(Collections.nCopies((pmFormNo - pmTypeNo), new Pokemon(pmTypes.get(0)))); //깊은 복사 아님
                pmTypes.addAll(Stream.generate(()-> new Pokemon(pmTypes.get(0))).limit(pmFormNo - pmTypeNo).toList()); //깊은 복사
                pmTypeNo = pmTypes.size(); //변경된 크기 다시 넣어주기
            } else if(pmFormNo < pmTypeNo) {
                //만약 form 정보보다 type 정보가 많을 경우 (메가진화체가 여러종류 있을 경우)
                //onePokemonInfos에 마지막 값을 다시 넣어준다.
//                onePokemonInfos.addAll(Collections.nCopies((pmTypeNo - pmFormNo), new Pokemon(onePokemonInfos.get(pmFormNo - 11)))); //깊은 복사 아님
                onePokemonInfos.addAll(Stream.generate(()-> new Pokemon(onePokemonInfos.get(pmFormNo - 1))).limit(pmTypeNo - pmFormNo).toList()); //깊은 복사
                goForTypeName = true;
            }

            for(int idx=0; idx<pmTypeNo; idx++) {
                Pokemon oneType = pmTypes.get(idx);
                Pokemon oneForm = onePokemonInfos.get(idx);
                //메가진화 폼이라면 타입에서 가져온 이름 사용하기 (Genesect 같은 예외 때문에 if문 추가함)
                if(oneForm.getPmForm().contains("Mega") || goForTypeName) oneForm.setPmForm(oneType.getPmForm());
                oneForm.setPmType1(oneType.getPmType1());
                oneForm.setPmType2(oneType.getPmType2());
                //passed-by-reference 라서 다시 set 해줄 필요 X
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
    private List<Pokemon> insertPokemon(List<Pokemon> onePokemonInfos) throws ParseException {
        int regionNum = 11;
        String formName = "일반";

        //DB에 넣을 값 담을 바구니 생성
        List<Pokemon> pokemons = new ArrayList<>();
        //폼 종류별로 다르게 지정하여 저장하기
        for(Pokemon oneForm : onePokemonInfos) {
            String pokemonFormName = oneForm.getPmForm();
            //폼 종류 중 리전폼, 메가진화 걸러내기
            switch(pokemonFormName.split(" ")[0]) {
                case "Mega":
                    formName = pokemonFormName.replace("Mega", "메가");
                    break;
                case "Alolan":
                    formName = Region.Galar.getKey();
                    regionNum = Region.Alola.ordinal();
                    break;
                case "Galarian":
                    formName = Region.Galar.getKey();
                    regionNum = Region.Galar.ordinal();
                    break;
                case "Hisuian":
                    formName = Region.Hisui.getKey();
                    regionNum = Region.Hisui.ordinal();
                    break;
                case "Paldean":
                    formName = Region.Paldea.getKey();
                    regionNum = Region.Paldea.ordinal();
                default:
                    if(!pokemonFormName.equals("Normal")) formName = pokemonFormName; //Normal 이 아닌 경우 전달된 폼 이름 넣기
                    regionNum = oneForm.getRegionSeq(); //가져온 리전 정보 그대로 넣어주기
            }
            oneForm.setPmForm(formName);
            oneForm.setRegionSeq(regionNum);
            
            pokemons.add(oneForm);
        }

        return repo.saveAll(pokemons);
    }


    /**
     * 포켓몬 릴리스 날짜 긁어서 받은 PMReleaseDates 에 넣어서 반환하기
     * @param pokemon
     * @param liList
     * @return
     */
    public Pokemon setReleaseDate(Pokemon pokemon, Elements liList) {
        return setReleaseDate(pokemon, liList, false);
    }

    /**
     * 포켓몬 릴리스 날짜 긁어서 받은 PMReleaseDates에 넣어서 반환하기
     * @param pokemon
     * @param liList
     * @return
     */
    public Pokemon setReleaseDate(Pokemon pokemon, Elements liList, boolean hasForms) {
        try {
            for (int i = 0; i < liList.size(); i++) {
                Element li = liList.get(i);
                Elements aTags = li.select("a");

                if (aTags.size() > 0) { //a 태그가 존재하는 li 태그에 한해서 날짜 입력 작업 실행
                    Elements liListForTesting = li.parent().select("li:not(:has(ul))");
                    LocalDate localDate = changeDateFormat(li.parent().select("li:nth-child(" + (i + 1) + ") > a:last-of-type").first().text());


                    if (aTags.text().contains("Shiny form")) { //이로치 정보
                        if (localDate == null && li.text().contains("on the same day")) {
                            //이로치 정보가 있지만 date 정보가 없을 경우 on the same day 로 표기되어있음 -> 릴리스 날짜와 일치함
                            localDate = pokemon.getPmReleaseDt();
                        }
                        pokemon.setShinyReleaseDt(localDate);
                    } else if (aTags.text().contains("Shadow form")) { //그림자 정보
                        //그림자 이로치 정보도 같이 있는 경우 제외하기 위해 select("a:last-of-type").first() 로 제어
                        pokemon.setShadowReleaseDt(localDate);
                    } else { //일반 릴리스 정보
                        if (pokemon.getPmReleaseDt() != null /*일반 미입력칸*/  /*form의 미입력칸 ---- 로 넣었었음*/)
                            continue; //이미 릴리스 정보가 있다면 다시 지정하지 않음
                        pokemon.setPmReleaseDt(localDate);
                    }
                }
                ;
            }
            return pokemon;
        } catch (Exception e) {
            System.out.println("[Error] pmName: "+pokemon.getPmForm()+", pokedexNum: "+pokemon.getPmPokedexNum());
            e.printStackTrace();
            return pokemon;
        }
    }

    /**
     * 날짜 형식 변환하기 
     * 웹페이지에서 갖고 있는 날짜형식을 yyyy-MM-dd 로 변경하기
     * @param dateText
     * @return
     */
    public LocalDate changeDateFormat(String dateText){
        //day에 붙은 ordinal indicator 제거
        dateText = dateText.replaceAll("(?<=\\d)(st|nd|rd|th)", "");

        try{
            DateTimeFormatter originDateFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(dateText, originDateFormat);

            return dateTime;
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
        List<String> exceptedPokemons = new ArrayList<>(Arrays.asList("Spinda"));

        return jq.select(Projections.constructor(GlobalPokemonName.class,
                pmName.languageSeq,
                pmName.pmPokedexNum,
                pmName.regionSeq,
                pmName.pmForm,
                pmName.pmNm
        )).from(pmName)
                .where(pmName.languageSeq.eq(2).and(pmName.pmNm.notIn(exceptedPokemons))) /*영어이름 가져오기*/
                .groupBy(pmName.pmPokedexNum)
                .fetch();
    }

    /**
     * 포켓몬 타입정보 가져오기
     * @param doc
     * @param pokedexNum
     * @return
     */
    public List<Pokemon> getPokemonType (Document doc, int pokedexNum) {
        List<Pokemon> pmTypes = new ArrayList<>();
        List<String> formNames = new ArrayList<>(
                doc
                .select(".poketab.multiinfobox > .tabber > .wds-tabs__wrapper > .wds-tabs > li")
                .stream().map(element -> element.attr("data-hash").replace("_", " ")).toList());
        Elements formTags = doc.select(".poketab.multiinfobox > .tabber > .wds-tab__content");
        if(formTags.size() == 0 && formNames.size() == 0) {
            //폼이 분리되어있지 않을 경우, 기본폼 넣어주기
            formTags.add(doc.selectFirst(".mw-parser-output > aside"));
            formNames.add(new String("Normal"));
        }
        for (int idx = 0; idx < formTags.size(); idx++) {
            Element form = formTags.get(idx);
            String formName = formNames.get(idx).replace("Regular", "Normal"); /*혹시나 Regular를 기본으로 사용한다면 Normal로 변경해주기*/
            String type1 = form.selectFirst("div[data-source=\"type1\"]").selectFirst("a:nth-child(2)").text();
            String type2 = form.selectFirst("div[data-source=\"type2\"]") == null ? null : form.selectFirst("div[data-source=\"type2\"]").selectFirst("a:nth-child(2)").text();
            pmTypes.add(new Pokemon(pokedexNum, formName, type1, type2));
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


