package com.movesmart.busdatamanager;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.jmolecules.archunit.JMoleculesArchitectureRules;
import org.jmolecules.archunit.JMoleculesDddRules;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

@AnalyzeClasses(
        packages = {
            "movesmart.busdatamanager.core",
            "movesmart.busdatamanager.notification",
            "movesmart.busdatamanager.passenger",
            "movesmart.busdatamanager.route",
            "movesmart.busdatamanager.vehicle"
        })
class ArchitectureTests {

    @ArchTest
    ArchRule dddRules = JMoleculesDddRules.all();

    @ArchTest
    ArchRule onion = JMoleculesArchitectureRules.ensureOnionSimple();

    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(BusDataManagerApplication.class);
        modules.forEach(System.out::println);
        modules.verify();
    }
}
