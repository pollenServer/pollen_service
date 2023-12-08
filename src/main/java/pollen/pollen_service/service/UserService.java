package pollen.pollen_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pollen.pollen_service.domain.InitResponse;
import pollen.pollen_service.domain.Oak;
import pollen.pollen_service.domain.Pine;
import pollen.pollen_service.domain.Weeds;
import pollen.pollen_service.repository.OakRepository;
import pollen.pollen_service.repository.PineRepository;
import pollen.pollen_service.repository.WeedsRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Value("${spring.service.secret_key}")
    private String SERVICEKEY;
    private final String CHARSET = "UTF-8";

    private final OakRepository oakRepository;
    private final PineRepository pineRepository;
    private final WeedsRepository weedsRepository;

    public Map<String, Object> initRequest() {
        Map<String, Object> map = new HashMap<>();

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        int month = now.getMonthValue();
        List<InitResponse> initResponse = new ArrayList<>();
        if (4 <= month && month <= 6) {
            List<Oak> findOakData = oakRepository.findInitData();
            List<Pine> findPineData = pineRepository.findInitData();

            findOakData.sort(Comparator.comparingLong(o -> Long.parseLong(o.getAreaNo())));
            findPineData.sort(Comparator.comparingLong(p -> Long.parseLong(p.getAreaNo())));

            for (int i = 0; i < findOakData.size() && i < findPineData.size(); i++) {
                Oak oak = findOakData.get(i);
                Pine pine = findPineData.get(i);
                initResponse.add(new InitResponse(oak.getAreaNo(), oak.getToday(), pine.getToday(), 0, Math.max(oak.getToday(), pine.getToday())));
            }
        }
        // 8~10월 => 잡초류
        if (8 <= month && month <= 12) {
            List<Weeds> initWeedsData = weedsRepository.findInitData();
            for (Weeds weeds : initWeedsData) {
                initResponse.add(new InitResponse(weeds.getAreaNo(), 0, 0, weeds.getToday(), weeds.getToday()));
            }
        }

        map.put("data", initResponse);
        return map;
    }

    public Object findOakPollen(String areaNo) throws IOException, ParseException {
        Oak oak = oakRepository.findByAreaNo(areaNo);
        if (oak == null) {
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
            String time = now.toString().replaceAll("-", "").concat("06");
            JSONObject result = getData("getOakPollenRiskndxV3", areaNo, time);
            if (result != null) {
                oak = new Oak(areaNo);
                if (result.get("today").toString().equals("")) {    // 전날 18시 데이터 응답 대비
                    oak.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                    oak.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                    oak.setTwodaysaftertomorrow(Integer.parseInt(result.get("twodaysaftertomorrow").toString()));
                } else {    // 당일 06시 데이터 응답
                    oak.setToday(Integer.parseInt(result.get("today").toString()));
                    oak.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                    oak.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                }
                oakRepository.save(oak);
                return oak;
            } else {
                return null;
            }
        } else {
            if (checkLastModifiedTime(oak.getLastModifiedTime())) {
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                String time = now.toLocalDate().toString().replaceAll("-", "").concat("06");
                JSONObject result = getData("getWeedsPollenRiskndxV3", areaNo, time);
                if (result != null) {
                    oak = new Oak(areaNo);
                    if (result.get("today").toString().equals("")) {    // 전날 18시 데이터 응답 대비
                        oak.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                        oak.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                        oak.setTwodaysaftertomorrow(Integer.parseInt(result.get("twodaysaftertomorrow").toString()));
                    } else {    // 당일 06시 데이터 응답
                        oak.setToday(Integer.parseInt(result.get("today").toString()));
                        oak.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                        oak.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                    }
                    oak.setLastModifiedTime(now);
                    return oak;
                } else {
                    return null;
                }
            }
        }
        return oak;
    }

    public Object findPinePollen(String areaNo) throws IOException, ParseException {
        Pine pine = pineRepository.findByAreaNo(areaNo);
        if (pine == null) {
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
            String time = now.toString().replaceAll("-", "").concat("06");
            JSONObject result = getData("getPinePollenRiskndxV3", areaNo, time);
            if (result != null) {
                pine = new Pine(areaNo);
                if (result.get("today").toString().equals("")) {    // 전날 18시 데이터 응답 대비
                    pine.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                    pine.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                    pine.setTwodaysaftertomorrow(Integer.parseInt(result.get("twodaysaftertomorrow").toString()));
                } else {    // 당일 06시 데이터 응답
                    pine.setToday(Integer.parseInt(result.get("today").toString()));
                    pine.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                    pine.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                }
                pineRepository.save(pine);
                return pine;
            } else {
                return null;
            }
        } else {
            if (checkLastModifiedTime(pine.getLastModifiedTime())) {
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                String time = now.toLocalDate().toString().replaceAll("-", "").concat("06");
                JSONObject result = getData("getWeedsPollenRiskndxV3", areaNo, time);
                if (result != null) {
                    pine = new Pine(areaNo);
                    if (result.get("today").toString().equals("")) {    // 전날 18시 데이터 응답 대비
                        pine.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                        pine.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                        pine.setTwodaysaftertomorrow(Integer.parseInt(result.get("twodaysaftertomorrow").toString()));
                    } else {    // 당일 06시 데이터 응답
                        pine.setToday(Integer.parseInt(result.get("today").toString()));
                        pine.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                        pine.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                    }
                    pine.setLastModifiedTime(now);
                    return pine;
                } else {
                    return null;
                }
            }
        }
        return pine;
    }

    public Object findWeedsPollen(String areaNo) throws IOException, ParseException {
        Weeds weeds = weedsRepository.findByAreaNo(areaNo);
        if (weeds == null) {
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
            String time = now.toString().replaceAll("-", "").concat("06");
            JSONObject result = getData("getWeedsPollenRiskndxV3", areaNo, time);
            if (result != null) {
                weeds = new Weeds(areaNo);
                if (result.get("today").toString().equals("")) {    // 전날 18시 데이터 응답 대비
                    weeds.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                    weeds.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                    weeds.setTwodaysaftertomorrow(Integer.parseInt(result.get("twodaysaftertomorrow").toString()));
                } else {    // 당일 06시 데이터 응답
                    weeds.setToday(Integer.parseInt(result.get("today").toString()));
                    weeds.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                    weeds.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                }
                weedsRepository.save(weeds);
                return weeds;
            }
        } else {
            if (checkLastModifiedTime(weeds.getLastModifiedTime())) {
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                String time = now.toLocalDate().toString().replaceAll("-", "").concat("06");
                JSONObject result = getData("getWeedsPollenRiskndxV3", areaNo, time);
                if (result != null) {
                    if (result.get("today").toString().equals("")) {    // 전날 18시 데이터 응답 대비
                        weeds.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                        weeds.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                        weeds.setTwodaysaftertomorrow(Integer.parseInt(result.get("twodaysaftertomorrow").toString()));
                    } else {    // 당일 06시 데이터 응답
                        weeds.setToday(Integer.parseInt(result.get("today").toString()));
                        weeds.setTomorrow(Integer.parseInt(result.get("tomorrow").toString()));
                        weeds.setDayaftertomorrow(Integer.parseInt(result.get("dayaftertomorrow").toString()));
                    }
                    weeds.setLastModifiedTime(now);
                    return weeds;
                } else {
                    return null;
                }
            }
        }
        return weeds;
    }

    public JSONObject getData(String url, String areaNo, String time) throws IOException, ParseException {
        String builtUrl = buildUrl(url, areaNo, time);
        JSONObject object = getJsonObject(builtUrl);
        if (object != null) {
            JSONObject response = (JSONObject) object.get("response");
            JSONObject header = (JSONObject) response.get("header");
            if (header.get("resultCode").equals("00")) {
                JSONObject body = (JSONObject) response.get("body");
                JSONObject items = (JSONObject) body.get("items");
                JSONArray item = (JSONArray) items.get("item");

                return (JSONObject) item.get(0);
            }
        }
        return null;
    }

    public JSONObject getJsonObject(String builtUrl) throws IOException, ParseException {
        URL url = new URL(builtUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json;utf-8");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject object = (JSONObject) jsonParser.parse(sb.toString());
            conn.disconnect();

            return object;
        } else {
            conn.disconnect();
            return null;
        }
    }

    public String buildUrl(String url, String areaNo, String time) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV3/" + url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", CHARSET) + "=" + SERVICEKEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", CHARSET) + "=" + URLEncoder.encode("1", CHARSET)); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", CHARSET) + "=" + URLEncoder.encode("10", CHARSET)); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", CHARSET) + "=" + URLEncoder.encode("JSON", CHARSET)); /*요청자료형식(XML/JSON)*/
        urlBuilder.append("&" + URLEncoder.encode("areaNo", CHARSET) + "=" + URLEncoder.encode(areaNo, CHARSET)); /*서울지점*/
        urlBuilder.append("&" + URLEncoder.encode("time", CHARSET) + "=" + URLEncoder.encode(time, CHARSET));    /*시간*/

        return urlBuilder.toString();
    }

    public boolean checkLastModifiedTime(LocalDateTime lastModifiedTime) {
        /**
         * 테스트를 위한 주석
         * 테스트 추가 : return true
         */
//        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
//        if (lastModifiedTime == null) return true;
//        return now.getDayOfMonth() != lastModifiedTime.getDayOfMonth();
        return false;
    }
}