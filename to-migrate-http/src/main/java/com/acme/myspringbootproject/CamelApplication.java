package com.acme.myspringbootproject;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class CamelApplication {

    public static void main(String[] args) {
        Quarkus.run(args);
    }

}
