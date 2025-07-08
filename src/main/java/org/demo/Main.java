package org.demo;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;

public class Main {
  public static void main(String[] args) {
    WireMockServer server =
        new WireMockServer(
            options()
                .port(8080)
                .extensions(new RandomAccessTransformer())
                .usingFilesUnderDirectory("src/main/resources"));

    server.stubFor(
        post(urlEqualTo("/soap-endpoint"))
            .willReturn(
                aResponse()
                    .withTransformers("random-access-transformer")
                    .withHeader("Content-Type", "text/xml")));
    server.start();

    System.out.println("WireMock running at http://localhost:8080");
  }
}
