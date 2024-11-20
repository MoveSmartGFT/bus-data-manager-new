package com.movessmart.busdatamanager;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.modulith.Modulithic;

@SpringBootApplication
@ConfigurationPropertiesScan
@Modulithic(
        systemName = "Bus Dataset Manager",
        sharedModules = {"com.movessmart.busdatamanager.core"},
        useFullyQualifiedModuleNames = true)
@Generated
@EnableCaching
public class BusDataManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusDataManagerApplication.class, args);
    }
}
