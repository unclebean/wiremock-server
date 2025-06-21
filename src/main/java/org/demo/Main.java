package org.demo;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;

public class Main {
  public static void main(String[] args) {
    WireMockServer server =
        new WireMockServer(options().port(8080).usingFilesUnderDirectory("src/main/resources"));

    server.start();

    System.out.println("WireMock running at http://localhost:8080");
  }
}
