package com.verdemar.service.ml;

import java.time.LocalDate;
import java.util.Map;

import com.verdemar.domain.dto.OccupancyDataPointDto;

public interface PredictionMLService {

    public Map<String, Integer> getBookingsPredictionsForYear(int year);

}
