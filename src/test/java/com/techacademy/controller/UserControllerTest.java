package com.techacademy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.techacademy.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    private final WebApplicationContext webApplicationContext;

    UserControllerTest(WebApplicationContext context) {
        this.webApplicationContext = context;
    }

    @BeforeEach
    void beforeEach() {
        // Spring Securityを有効にする
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }



    @Test
    @DisplayName("User一覧画面")
    @WithMockUser
    void testGetList() throws Exception {
          // HTTPリクエストに対するレスポンスの検証
          MvcResult result = mockMvc.perform(get("/user/list")) // URLにアクセス
              .andExpect(status().isOk()) // ステータスを確認
              .andExpect(model().attributeExists("userlist")) // Modelの内容を確認
              .andExpect(model().hasNoErrors()) // Modelのエラー有無の確認
              .andExpect(view().name("user/list")) // viewの確認
              .andReturn(); // 内容の取得

          // userの検証
          // Modelからuserを取り出す
          List<User> userlist = (List<User>)result.getModelAndView().getModel().get("userlist");
          assertEquals(1, userlist.get(0).getId());
          assertEquals("キラメキ太郎", userlist.get(0).getName());
          assertEquals(2, userlist.get(1).getId());
          assertEquals("キラメキ次郎", userlist.get(1).getName());
          assertEquals(3, userlist.get(2).getId());
          assertEquals("キラメキ花子", userlist.get(2).getName());

          assertEquals(3, userlist.size());
      }
  }
