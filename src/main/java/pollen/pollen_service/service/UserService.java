package pollen.pollen_service.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@Slf4j
public class UserService {

    @Value("${spring.service.secret_key}")
    private String SERVICEKEY;

    private final String CHARSET = "UTF-8";

    public Object findOakPollen(String areaNo, String time) throws IOException, ParseException {
        String builtUrl = buildUrl("getOakPollenRiskndxV3", areaNo, time);
        JSONObject object = getJsonObject(builtUrl);

        return object;
    }
    
    public Object findPinePollen(String areaNo, String time) throws IOException, ParseException {
        String builtUrl = buildUrl("getPinePollenRiskndxV3", areaNo, time);
        JSONObject object = getJsonObject(builtUrl);

        return object;
    }
    
    public Object findWeedsPollen(String areaNo, String time) throws IOException, ParseException {
        String builtUrl = buildUrl("getWeedsPollenRiskndxV3", areaNo, time);
        JSONObject object = getJsonObject(builtUrl);

        return object;
    }

    public JSONObject getJsonObject(String builtUrl) throws IOException, ParseException {
        URL url = new URL(builtUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        JSONParser jsonParser = new JSONParser();
        JSONObject object = (JSONObject) jsonParser.parse(new InputStreamReader(conn.getInputStream(), CHARSET));
        conn.disconnect();
        return object;
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