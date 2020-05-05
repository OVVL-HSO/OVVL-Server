package com.tam;

import com.tam.services.meta.CPEService;
import com.tam.services.meta.CVEService;
import com.tam.services.meta.CWEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@ComponentScan //(basePackages = { "io.swagger", "io.swagger.api" })
@EnableAutoConfiguration
@EnableSwagger2
@SpringBootApplication
@EnableCaching
public class ThreatServer implements CommandLineRunner {

    @Autowired
    private CVEService cveService;

    @Autowired
    private CPEService cpeService;

    @Autowired
    private CWEService cweService;


    public static void main(String[] args) throws Exception {
        new SpringApplication(ThreatServer.class).run(args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        // cpeService.fillDBWithCPEXML();
        // cveService.fillDBWithCVEData();
        // cweService.fillDBWithCWEData();
        cweService.fillDBCWEThreatCatalogue();

        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
