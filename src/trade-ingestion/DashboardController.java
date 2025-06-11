package trade.ingestion;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        // Serve the dashboard HTML page
        return "dashboard";
    }
}

@RestController
@RequestMapping("/api")
class DashboardApiController {

    @GetMapping(value = "/latency", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody byte[] getLatencyData() throws IOException {
        ClassPathResource resource = new ClassPathResource("ui-dashboard/latency.json");
        return Files.readAllBytes(Path.of(resource.getURI()));
    }

    @GetMapping(value = "/throughput", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody byte[] getThroughputData() throws IOException {
        ClassPathResource resource = new ClassPathResource("ui-dashboard/throughput.json");
        return Files.readAllBytes(Path.of(resource.getURI()));
    }
}
