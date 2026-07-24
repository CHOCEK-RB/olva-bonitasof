package com.olva.enviosapi.application.service;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BonitaService {

  private final RestTemplate restTemplate;

  @Value("${bonita.api.url}")
  private String bonitaUrl;

  @Value("${bonita.api.username}")
  private String username;

  @Value("${bonita.api.password}")
  private String password;

  @Value("${bonita.api.process.name}")
  private String processName;

  public BonitaService() {
    this.restTemplate = new RestTemplate();
  }

  public void instanciarProceso(String tracking) {
    String loginUrl = bonitaUrl + "/loginservice";

    HttpHeaders loginHeaders = new HttpHeaders();
    loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> loginBody = new LinkedMultiValueMap<>();
    loginBody.add("username", username);
    loginBody.add("password", password);
    loginBody.add("redirect", "false");

    HttpEntity<MultiValueMap<String, String>> loginRequest = new HttpEntity<>(loginBody, loginHeaders);
    ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, String.class);

    List<String> cookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
    if (cookies == null || cookies.isEmpty()) {
      throw new RuntimeException("No se recibieron cookies del login de Bonita");
    }

    String apiToken = null;
    for (String cookie : cookies) {
      if (cookie.contains("X-Bonita-API-Token=")) {
        Matcher matcher = Pattern.compile("X-Bonita-API-Token=([^;]+)").matcher(cookie);
        if (matcher.find()) {
          apiToken = matcher.group(1);
        }
      }
    }

    if (apiToken == null) {
      throw new RuntimeException("No se encontró X-Bonita-API-Token en las cookies");
    }

    String searchProcessUrl = bonitaUrl + "/API/bpm/process?f=name=" + processName + "&p=0&c=10";
    HttpHeaders apiHeaders = new HttpHeaders();
    apiHeaders.put(HttpHeaders.COOKIE, cookies);
    apiHeaders.set("X-Bonita-API-Token", apiToken);
    apiHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Void> searchRequest = new HttpEntity<>(null, apiHeaders);
    ResponseEntity<List<Map<String, Object>>> searchResponse = restTemplate.exchange(
        searchProcessUrl,
        HttpMethod.GET,
        searchRequest,
        new ParameterizedTypeReference<List<Map<String, Object>>>() {
        });

    List<Map<String, Object>> procesos = searchResponse.getBody();
    if (procesos == null || procesos.isEmpty()) {
      throw new RuntimeException("No se encontró el proceso " + processName + " desplegado en Bonita.");
    }

    String processId = String.valueOf(procesos.get(0).get("id"));
    System.out.println("✅ ID de Proceso encontrado: " + processId);

    String instanciateUrl = bonitaUrl + "/API/bpm/process/" + processId + "/instantiation";
    Map<String, String> contract = new HashMap<>();
    contract.put("trackingInput", tracking);

    HttpEntity<Map<String, String>> instanciateRequest = new HttpEntity<>(contract, apiHeaders);
    restTemplate.postForEntity(instanciateUrl, instanciateRequest, String.class);

    System.out.println("✅ Proceso de Almacen instanciado correctamente en Bonita para el tracking: " + tracking);
  }
}
