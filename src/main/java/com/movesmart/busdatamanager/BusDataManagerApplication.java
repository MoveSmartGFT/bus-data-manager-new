package com.movesmart.busdatamanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.modulith.Modulithic;
import lombok.Generated;

@SpringBootApplication
@ConfigurationPropertiesScan
@Modulithic(
		systemName = "Bus Dataset Manager",
		sharedModules = {"com.movesmart.busdatamanager.core"},
		useFullyQualifiedModuleNames = true
)
@Generated
public class BusDataManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusDataManagerApplication.class, args);
	}

}
