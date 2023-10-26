package com.beachforecast.api.beachforecastapi.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.beachforecast.api.beachforecastapi.entities.Criterion;
import com.beachforecast.api.beachforecastapi.entities.Rating.RatingType;
import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;
import com.beachforecast.api.beachforecastapi.models.BeachForecast;
import com.beachforecast.api.beachforecastapi.models.ForecastPoint;
import com.beachforecast.api.beachforecastapi.models.ForecastRating;
import com.beachforecast.api.beachforecastapi.models.TimeForecast;
import com.beachforecast.api.beachforecastapi.usecases.forecast.ProcessForecastsForBeachesUseCase;
import com.beachforecast.api.beachforecastapi.utils.DefaultRatings;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static com.beachforecast.api.beachforecastapi.utils.ControllersTestsHelper.*;

@WebMvcTest(ForecastController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class ForecastControllerTest {
  @MockBean
  private ProcessForecastsForBeachesUseCase processForecastsForBeachesUseCase;

  @Autowired
  MockMvc mockMvc;

  @Test
  public void should_be_able_to_process_forecasts() throws Exception {
    var userInfo = generatUserInfo();
    var timeForecasts = List.of(generateTimeForecast(userInfo));

    Mockito
        .when(processForecastsForBeachesUseCase.execute(userInfo.getSub()))
        .thenReturn(timeForecasts);

    mockMvc
        .perform(get("/forecasts").with(jwtRequestPostProcessor(userInfo)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(timeForecasts.size()))
        .andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  private TimeForecast generateTimeForecast(UserInfo userInfo) {
    var beachForecast = generateBeachForecast(userInfo);

    return new TimeForecast(beachForecast.getTime(), List.of(beachForecast));
  }

  private BeachForecast generateBeachForecast(UserInfo userInfo) {
    var point = generateForecastPoint();
    var beach = generateBeach(userInfo);
    var forecastRatings = generateForecastRatings(userInfo.getSub());
    beach.setId(UUID.randomUUID());

    return new BeachForecast(point, beach, forecastRatings);
  }

  private ForecastPoint generateForecastPoint() {
    return new ForecastPoint(LocalDateTime.now().toString(), 139, 1.04F, 9.75F, 139, 1.04F, 9.75F, 8, 7.6F, 24140.0F,26.3F);
  }

  private List<ForecastRating> generateForecastRatings(UUID userId) {
    return DefaultRatings
        .generateDefaultRatings(userId)
        .stream()
        .collect(Collectors.toList())
        .stream()
        .map(rating -> {
          rating.setId(UUID.randomUUID());
          rating.setCriteria(populateCriteriaIds(rating.getCriteria()));
          return rating;
        })
        .map(rating -> {
          int value = rating.getType() == RatingType.BEACHGOER ? 1 : 4;
          return new ForecastRating(rating.getId(), rating.getName(), rating.getType(), rating.getCriteria(), value);
        })
        .collect(Collectors.toList());
  }

  private Set<Criterion> populateCriteriaIds(Set<Criterion> criteria) {
    return criteria.stream()
        .map(criterion -> {
          criterion.setId(UUID.randomUUID());
          return criterion;
        })
        .collect(Collectors.toSet());
  }
}
