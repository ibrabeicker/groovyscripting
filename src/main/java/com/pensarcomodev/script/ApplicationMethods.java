package com.pensarcomodev.script;

import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class ApplicationMethods {

    public void logMethod(String parameter) {
        System.out.println("Calling string method: " + parameter);
    }

    public void logMethod(int parameter) {
        System.out.println("Calling number method: " + parameter);
    }

    public void logMethod(BigDecimal parameter) {
        System.out.println("Calling number method: " + parameter);
    }
}
