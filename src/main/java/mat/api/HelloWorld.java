package mat.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @GetMapping("/api/hello")
    public Map<String, String> get() {
        Map<String, String> map = new HashMap<>();
        map.put("value", "Hello, World");
        return map;
    }
}
