package pt.ipleiria.estg.dei.ei.dae.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class IntelliPacApplication extends Application {
    static ObjectMapper mapper = new ObjectMapper();

    static {
        // Configure the ObjectMapper with modules
        mapper.findAndRegisterModules();
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }
}