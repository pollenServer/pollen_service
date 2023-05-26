package pollen.pollen_service.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private String SERVICEKEY = "test-key";

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void before(){
        ReflectionTestUtils.setField(userService, "SERVICEKEY", SERVICEKEY);
    }

    @Test
    void buildOakUrlSuccess() throws UnsupportedEncodingException {
        //given
        String url = "getOakPollenRiskIdxV3";
        String areaNo = "11000000";
        String time = "2023052606";

        //when
        String builtUrl = userService.buildUrl(url, areaNo, time);

        //then
        assertThat(builtUrl).isEqualTo("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV3/" + url +
                "?serviceKey=" + SERVICEKEY +
                "&pageNo=1" +
                "&numOfRows=10" +
                "&dataType=JSON" +
                "&areaNo=" + areaNo +
                "&time=" + time);
    }
    @Test
    void buildPineUrlSuccess() throws UnsupportedEncodingException {
        //given
        String url = "getPinePollenRiskIdxV3";
        String areaNo = "11000000";
        String time = "2023052606";

        //when
        String builtUrl = userService.buildUrl(url, areaNo, time);

        //then
        assertThat(builtUrl).isEqualTo("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV3/" + url +
                "?serviceKey=" + SERVICEKEY +
                "&pageNo=1" +
                "&numOfRows=10" +
                "&dataType=JSON" +
                "&areaNo=" + areaNo +
                "&time=" + time);
    }
    @Test
    void buildWeedsUrlSuccess() throws UnsupportedEncodingException {
        //given
        String url = "getWeedsPollenRiskIdxV3";
        String areaNo = "11000000";
        String time = "2023052606";

        //when
        String builtUrl = userService.buildUrl(url, areaNo, time);

        //then
        assertThat(builtUrl).isEqualTo("http://apis.data.go.kr/1360000/HealthWthrIdxServiceV3/" + url +
                "?serviceKey=" + SERVICEKEY +
                "&pageNo=1" +
                "&numOfRows=10" +
                "&dataType=JSON" +
                "&areaNo=" + areaNo +
                "&time=" + time);
    }
}