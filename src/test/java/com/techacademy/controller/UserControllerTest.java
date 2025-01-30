package com.techacademy.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.test.context.support.WithMockUser;

import com.techacademy.entity.User;
import com.techacademy.entity.User.Gender;
import com.techacademy.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@WithMockUser(username="testuser", roles={"USER"})  // 認証バイパス
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // Spring のテストコンテキストに登録
    private UserService userService;

    @Mock  // モデルのモック
    private Model model;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito を初期化

        User user1 = new User();
        user1.setId(1);
        user1.setName("キラメキ太郎");

        User user2 = new User();
        user2.setId(2);
        user2.setName("キラメキ次郎");

        User user3 = new User();
        user3.setId(3);
        user3.setName("キラメキ花子");

        userList = Arrays.asList(user1, user2, user3);
        when(userService.getUserList()).thenReturn(userList);
    }

    @Test
    void testGetList() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())  // HTTPステータスが200であること
                .andExpect(model().attributeExists("userlist")) // Modelにuserlistが含まれること
                .andExpect(model().attribute("userlist", hasSize(3))) // userlistの件数が3件であること
                .andExpect(model().hasNoErrors()) // Modelにエラーが無いこと
                .andExpect(view().name("user/list")); // viewの名前が "user/list" であること

        // Modelのデータを取得して検証
        when(model.getAttribute("userlist")).thenReturn(userList);
        List<User> retrievedUsers = (List<User>) model.getAttribute("userlist");
        assertThat(retrievedUsers).isNotNull();
        assertThat(retrievedUsers).hasSize(3);

        // userlist の各要素を検証（1件ずつ取り出してチェック）
        for (int i = 0; i < userList.size(); i++) {
            assertThat(retrievedUsers.get(i).getId()).isEqualTo(userList.get(i).getId());
            assertThat(retrievedUsers.get(i).getName()).isEqualTo(userList.get(i).getName());
        }
    }
}
