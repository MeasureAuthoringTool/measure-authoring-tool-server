package mat.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class API {

    @ResponseBody
    @GetMapping(value = "/api", produces="application/json")
    public Map<String, String> get() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Measure Authoring Tool API");
        map.put("version", "v0.0.1");

        return map;
    }
}
