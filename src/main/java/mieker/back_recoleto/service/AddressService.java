package mieker.back_recoleto.service;

import mieker.back_recoleto.entity.response.ResponseGeoCodeAPI;
import mieker.back_recoleto.entity.response.ResponseViaCepAPI;
import mieker.back_recoleto.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class AddressService {
    private final String urlAPIViaCep = "https://viacep.com.br/ws/";
    private final String geocodeAPI = "https://geocode.maps.co/search";

    @Value("${api-key}")
    private String apiKey;

    private ResponseGeoCodeAPI turnStreetToGeocode(String cep, String num) {
        ResponseViaCepAPI cepAPI = this.turnCepToStreet(cep);
//        String urlGeocode = URLEncoder.encode(street + ", " + num, StandardCharsets.UTF_8);
        String urlGeocode = "?street=";
        urlGeocode += num;
        urlGeocode += "+" + cepAPI.getLogradouro();
        urlGeocode += "&city=" + cepAPI.getLocalidade();
        urlGeocode += "&state=" + cepAPI.getUf();
        urlGeocode += "&country=" + "Brazil";
        urlGeocode += "&api_key=" + apiKey;
        System.out.println("Generated URL: " + urlGeocode); // Log para debug

        List<ResponseGeoCodeAPI> response = this.fetchGeocodeDataFromGeoCode(urlGeocode);

        if (!response.isEmpty()) {
            ResponseGeoCodeAPI firstResult = response.get(0); // Pega o primeiro resultado
            System.out.println("First Result: " + firstResult); // Debug
            ResponseGeoCodeAPI responseGeoCodeAPI = new ResponseGeoCodeAPI();
            responseGeoCodeAPI.setLat(firstResult.getLat());
            responseGeoCodeAPI.setLon(firstResult.getLon());
            return responseGeoCodeAPI; // Retorna latitude e longitude
        } else {
            System.out.println("No results found.");
            throw new NotFoundException("Endereço não encontrado.");
        }
    }

    private List<ResponseGeoCodeAPI> fetchGeocodeDataFromGeoCode(String url) {
        try {
            String responseBody = WebClient
                    .create(geocodeAPI)
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class) // Recebe como String para debug
                    .block();

            System.out.println("Raw API Response: " + responseBody);

            ResponseGeoCodeAPI[] responseArray = WebClient
                    .create(geocodeAPI)
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(ResponseGeoCodeAPI[].class) // Parse como array de objetos
                    .block();

//            System.out.println("Raw API Response: " + responseBody);

            if (responseArray != null) {
                System.out.println("Response received with " + responseArray.length + " items."); // Debug
                return List.of(responseArray); // Converte o array para uma lista
            } else {
                System.out.println("Response is null.");
                return List.of(); // Retorna uma lista vazia
            }
        } catch (Exception e) {
            System.err.println("Error in fetchGeocodeDataFromGeoCode: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Retorna uma lista vazia em caso de erro
        }
    }



    private ResponseViaCepAPI turnCepToStreet(String cep) {
        String urlViaCep = cep + "/json";
        ResponseViaCepAPI response = this.fetchAddressDataFromViaCEP(urlViaCep);
//        System.out.println("Response: " + response); // Debug
        if (response.getCep() == null) {
            throw new NotFoundException("CEP não encontrado.");
        }
//        assert response != null;
        response.setLocalidade(response.getLocalidade().replace(" ", "+").toLowerCase());
        response.setLogradouro(response.getLogradouro().replace(" ", "+").toLowerCase());
        return response;
    }

    private ResponseViaCepAPI fetchAddressDataFromViaCEP(String url) {
        return WebClient
                .create(urlAPIViaCep)
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ResponseViaCepAPI.class)
                .block();
    }

    public ResponseGeoCodeAPI getAddress(String cep, String num) {
        System.out.println(cep);
//        this.turnStreetToGeocode(cep, num);
        return this.turnStreetToGeocode(cep, num);
    }
}
