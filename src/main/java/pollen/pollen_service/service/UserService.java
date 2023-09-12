package pollen.pollen_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.service.secret_key}")
    private String SERVICEKEY;
    private final String CHARSET = "UTF-8";

    private final OakRepository oakRepository;
    private final PineRepository pineRepository;
    private final WeedsRepository weedsRepository;

    public Object findOakPollen(String areaNo, String time) throws IOException, ParseException {
        Oak oak = oakRepository.findByAreaNo(areaNo);
        if (oak == null) {
            String builtUrl = buildUrl("getOakPollenRiskndxV3", areaNo, time);
            return getJsonObject(builtUrl);
        } else {
            return oak;
        }
    }
    
    public Object findPinePollen(String areaNo, String time) throws IOException, ParseException {
        Pine pine = pineRepository.findByAreaNo(areaNo);
        if (pine == null) {
            String builtUrl = buildUrl("getPinePollenRiskndxV3", areaNo, time);
            return getJsonObject(builtUrl);
        } else {
            return pine;
        }
    }
    
    public Object findWeedsPollen(String areaNo, String time) throws IOException, ParseException {
        Weeds weeds = weedsRepository.findByAreaNo(areaNo);
        if (weeds == null) {
            String builtUrl = buildUrl("getWeedsPollenRiskndxV3", areaNo, time);
            return getJsonObject(builtUrl);
        } else {
            return weeds;
        }
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
        urlBuilder.append("?" + URLEncoder.encode("serviceKey",CHARSET) + "=" + SERVICEKEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo",CHARSET) + "=" + URLEncoder.encode("1", CHARSET)); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows",CHARSET) + "=" + URLEncoder.encode("10", CHARSET)); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType",CHARSET) + "=" + URLEncoder.encode("JSON", CHARSET)); /*요청자료형식(XML/JSON)*/
        urlBuilder.append("&" + URLEncoder.encode("areaNo",CHARSET) + "=" + URLEncoder.encode(areaNo, CHARSET)); /*서울지점*/
        urlBuilder.append("&" + URLEncoder.encode("time",CHARSET) + "=" + URLEncoder.encode(time, CHARSET));    /*시간*/

        return urlBuilder.toString();
    }
}