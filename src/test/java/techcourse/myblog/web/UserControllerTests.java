package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    private String cookie;
    private static long userId = 1;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password1234!")
                        .with("name", "name"))
                .exchange();

        log.info("cookie : {}", cookie);

        cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password1234!"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");

        log.info("cookie : {}", cookie);
    }

    @Test
    void 회원가입_페이지_이동_테스트() {
        webTestClient.get().uri("/signup")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원가입_요청_이메일_중복_실패_테스트() {
        String duplicatedEmail = "email@gmail.com";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", duplicatedEmail)
                        .with("password", "password1234!")
                        .with("name", "name"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이름_형식_실패_테스트() {
        String wrongName = "a";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", "password")
                        .with("name", wrongName))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_패스워드_실패_테스트() {
        String wrongPassword = "password";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", "email@gmail.com")
                        .with("password", wrongPassword)
                        .with("name", "name"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원가입_요청_이메일_형식_실패_테스트() {
        String wrongEmail = "email";

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("email", wrongEmail)
                        .with("password", "password1234!")
                        .with("name", "name"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void 회원_정보_전체_조회_테스트() {
        webTestClient.get().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void MyPage_이동_테스트() {
        webTestClient.get().uri("/mypage/" + userId)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void EditMyPage_이동_성공_테스트() {
        webTestClient.get().uri("/mypage/" + userId + "/edit")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 회원_정보_수정_성공_테스트() {
        webTestClient.put().uri("/users/" + userId)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "newName"))
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void 회원_정보_수정_실패_테스트() {
        webTestClient.put().uri("/users/" + userId)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", ""))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @AfterEach
    void 회원_탈퇴_성공_테스트() {
        webTestClient.delete().uri("/users/" + userId++)
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().isFound();
    }
}
