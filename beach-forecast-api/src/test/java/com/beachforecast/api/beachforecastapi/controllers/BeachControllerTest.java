package com.beachforecast.api.beachforecastapi.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.beachforecast.api.beachforecastapi.usecases.beach.ListUserBeachesUseCase;
import com.beachforecast.api.beachforecastapi.usecases.beach.SaveBeachUseCase;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static com.beachforecast.api.beachforecastapi.utils.ControllersTestsHelper.*;

import java.util.List;

@WebMvcTest(BeachController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class BeachControllerTest {
  @MockBean
  private ListUserBeachesUseCase listUserBeachesUseCase;

  @MockBean
  private SaveBeachUseCase saveBeachUseCase;

  @Autowired
  MockMvc mockMvc;

  @Test
  public void should_return_a_list_with_user_beaches() throws Exception {
    var userInfo = generatUserInfo();
    var mockBeaches = List.of(generateBeach(userInfo));

    Mockito
        .when(listUserBeachesUseCase.execute(userInfo.getSub()))
        .thenReturn(mockBeaches);

    mockMvc
        .perform(get("/beaches").with(jwtRequestPostProcessor(userInfo)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(mockBeaches.size()))
        .andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  public void should_be_able_to_save_a_beach() throws Exception {
    var userInfo = generatUserInfo();
    var mockBeachDto = generateBeachDto();
    var mockBeach = generateBeach(userInfo);
    var mockBeachDtoJson = objectMapper.writeValueAsString(mockBeachDto);

    Mockito
        .when(saveBeachUseCase.execute(mockBeachDto, userInfo.getSub()))
        .thenReturn(mockBeach);

    mockMvc
        .perform(
            post("/beaches")
                .with(jwtRequestPostProcessor(userInfo))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(mockBeachDtoJson)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(mockBeach.getName()))
        .andExpect(jsonPath("$.lat").value(mockBeach.getLat()))
        .andExpect(jsonPath("$.lng").value(mockBeach.getLng()))
        .andExpect(jsonPath("$.userId").value(mockBeach.getUserId().toString()))
        .andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  @Test
  public void should_return_unauthorized_status_for_a_request_without_jwt() throws Exception {
    mockMvc
        .perform(get("/beaches"))
        .andDo(print())
        .andExpect(status().isUnauthorized())
        .andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }
}
