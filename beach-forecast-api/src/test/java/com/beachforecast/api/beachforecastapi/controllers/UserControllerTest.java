package com.beachforecast.api.beachforecastapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.beachforecast.api.beachforecastapi.infra.security.UserInfo;
import com.beachforecast.api.beachforecastapi.models.UserData;
import com.beachforecast.api.beachforecastapi.usecases.user.GetUserDataUseCase;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static com.beachforecast.api.beachforecastapi.utils.ControllersTestsHelper.*;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class UserControllerTest {
  @MockBean
  private GetUserDataUseCase getUserDataUseCase;

  @Autowired
  MockMvc mockMvc;

  @Test
  public void should_be_able_get_user_data() throws Exception {
    var userInfo = generatUserInfo();
    var userData = generateUserData(userInfo);

    Mockito
        .when(getUserDataUseCase.execute(userInfo.getSub()))
        .thenReturn(userData);

    mockMvc
        .perform(get("/users/me").with(jwtRequestPostProcessor(userInfo)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userInfo.getSub().toString()))
        .andDo(document("{methodName}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  private UserData generateUserData(UserInfo userInfo) {
    return new UserData(userInfo.getSub(), userInfo.getPreferredUsername(), true, userInfo.isEmailVerified(),
        userInfo.getEmail(), userInfo.getGivenName(), userInfo.getFamilyName());
  }
}
