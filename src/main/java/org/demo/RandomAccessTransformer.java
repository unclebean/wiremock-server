package org.demo;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.*;
import java.io.*;
import java.util.Random;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class RandomAccessTransformer extends ResponseTransformer {

  private final Random random = new Random();

  @Override
  public String getName() {
    return "random-access-transformer";
  }

  @Override
  public boolean applyGlobally() {
    return false;
  }

  @Override
  public Response transform(
      Request request, Response response, FileSource fileSource, Parameters parameters) {
    try {
      String body = request.getBodyAsString();

      // Parse XML
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(body.getBytes()));

      NodeList ids = doc.getElementsByTagName("id");

      String resultXml = "";
      for (int i = 0; i < ids.getLength(); i++) {
        String idValue = ids.item(i).getTextContent();
        boolean allowed = random.nextBoolean();
        resultXml +=
            "<result><id>"
                + idValue
                + "</id><status>"
                + (allowed ? "allowed" : "denied")
                + "</status></result>\n";
      }

      String fullResponse =
          "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>\n"
              + "  <soapenv:Body>\n"
              + "    <checkAccessResponse>\n"
              + resultXml
              + "    </checkAccessResponse>\n"
              + "  </soapenv:Body>\n"
              + "</soapenv:Envelope>";

      return Response.response()
          .status(200)
          .headers(response.getHeaders())
          .body(fullResponse)
          .build();

    } catch (Exception e) {
      e.printStackTrace();
      return Response.response().status(500).body("Error processing XML").build();
    }
  }
}
