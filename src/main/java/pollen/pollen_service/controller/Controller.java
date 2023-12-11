package pollen.pollen_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pollen.pollen_service.service.UserService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final UserService userService;

    @GetMapping("/oak")
    public Object oakRequest(@RequestParam("areaNo") String areaNo) throws IOException, ParseException {
        return userService.findOakPollen(areaNo);
    }

    @GetMapping("/pine")
    public Object pineRequest(@RequestParam("areaNo") String areaNo) throws IOException, ParseException {
        return userService.findPinePollen(areaNo);
    }

    @GetMapping("/weeds")
    public Object weedsRequest(@RequestParam("areaNo") String areaNo) throws IOException, ParseException {
        return userService.findWeedsPollen(areaNo);
    }

    @GetMapping("/init")
    public Map<String, Object> initRequest() {
        return userService.initRequest();
    }
}
