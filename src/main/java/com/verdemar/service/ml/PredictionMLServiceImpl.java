package com.verdemar.service.ml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictionMLServiceImpl implements PredictionMLService {

    private final WebClient webClient;

    public PredictionMLServiceImpl(@Value("${model}") String aiBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(aiBaseUrl).build();
    }

    @Override
    public Map<String, Integer> getBookingsPredictionsForYear(int year) {
        String startStr = year + "-01-01";
        String endStr = year + "-12-31";

        Map<String, Integer> predictionMap = new HashMap<>();
        try {
            // Hacemos la llamada bloqueante a FastAPI
            FastApiResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/bookings/predict-range")
                            .queryParam("start_date", startStr)
                            .queryParam("end_date", endStr)
                            .build())
                    .retrieve()
                    .bodyToMono(FastApiResponse.class)
                    .block();

            // Debug en consola para ver si el objeto mapea algo
            if (response != null) {
                System.out.println("--> [IA SERVICE] Respuesta cruda recibida de WebClient.");
                if (response.getPredictions() != null) {
                    System.out.println(
                            "--> [IA SERVICE] Número de predicciones mapeadas: " + response.getPredictions().size());

                    for (FastApiPrediction pred : response.getPredictions()) {
                        if (pred.getDate() != null) {
                            predictionMap.put(pred.getDate(), pred.getPredictedBookings());
                        }
                    }
                } else {
                    System.err.println(
                            "--> [IA SERVICE] ¡ATENCIÓN! La lista 'predictions' dentro del objeto viene NULL.");
                }
            } else {
                System.err.println("--> [IA SERVICE] ¡ATENCIÓN! El objeto 'response' entero es NULL.");
            }

        } catch (Exception e) {
            System.err.println("Error crítico al conectar o parsear FastAPI: " + e.getMessage());
            e.printStackTrace();
        }
        return predictionMap;
    }

    // =========================================================================
    // DTOs CAMBIADOS A PUBLIC Y CON PROTECCIÓN ANTE PROPIEDADES DESCONOCIDAS
    // =========================================================================

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FastApiResponse {
        private List<FastApiPrediction> predictions;

        public FastApiResponse() {
        } // Constructor vacío para Jackson

        public List<FastApiPrediction> getPredictions() {
            return predictions;
        }

        public void setPredictions(List<FastApiPrediction> predictions) {
            this.predictions = predictions;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FastApiPrediction {
        @JsonProperty("date")
        private String date;

        @JsonProperty("predicted_bookings")
        private int predictedBookings;

        public FastApiPrediction() {
        } // Constructor vacío para Jackson

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getPredictedBookings() {
            return predictedBookings;
        }

        public void setPredictedBookings(int predictedBookings) {
            this.predictedBookings = predictedBookings;
        }
    }
}