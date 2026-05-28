package com.verdemar.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OccupancyDataPointDto {

    private LocalDate date;

    @JsonProperty("actual_occupancy_rate")
    private Double actualOccupancyRate;

    @JsonProperty("actual_booked_apartments")
    private Integer actualBookedApartments;

    @JsonProperty("actual_total_apartments")
    private Integer actualTotalApartments;

    @JsonProperty("actual_revenue")
    private BigDecimal actualRevenue;

    @JsonProperty("predicted_occupancy_rate")
    private Double predictedOccupancyRate;

    @JsonProperty("predicted_booked_apartments")
    private Integer predictedBookedApartments;

    @JsonProperty("predicted_revenue")
    private BigDecimal predictedRevenue;

    @JsonProperty("prediction_confidence")
    private Double predictionConfidence;

    @JsonProperty("is_historical")
    private boolean isHistorical;

    @JsonProperty("is_prediction")
    private boolean isPrediction;
}