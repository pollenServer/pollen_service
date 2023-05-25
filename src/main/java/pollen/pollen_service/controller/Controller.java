package pollen.pollen_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pollen.pollen_service.service.UserService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final UserService userService;

    @GetMapping("/oak")
    public Object oakRequest(@RequestParam("areaNo") String areaNo, @RequestParam("time") String time) throws IOException, ParseException {
        return userService.findOakPollen(areaNo, time);
    }

    @GetMapping("/pine")
    public Object pineRequest(@RequestParam("areaNo") String areaNo, @RequestParam("time") String time) throws IOException, ParseException {
        return userService.findPinePollen(areaNo, time);
    }

    @GetMapping("/weeds")
    public Object weedsRequest(@RequestParam("areaNo") String areaNo, @RequestParam("time") String time) throws IOException, ParseException {
        return userService.findWeedsPollen(areaNo, time);
    }
}
