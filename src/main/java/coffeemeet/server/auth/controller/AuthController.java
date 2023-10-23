package coffeemeet.server.auth.controller;

import coffeemeet.server.auth.dto.SignupRequest;
import coffeemeet.server.auth.service.AuthService;
import coffeemeet.server.auth.utils.AuthTokens;
import coffeemeet.server.user.domain.OAuthProvider;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/auth")
public class AuthController {

  private final AuthService authService;

  @GetMapping("/{oAuthProvider}")
  public ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OAuthProvider oAuthProvider,
      HttpServletResponse response) throws IOException {
    String redirectUrl = authService.getAuthCodeRequestUrl(oAuthProvider);
    response.sendRedirect(redirectUrl);
    return new ResponseEntity<>(HttpStatus.FOUND);
  }

  @PostMapping("/sign-up")
  public ResponseEntity<AuthTokens> signup(@RequestBody SignupRequest request) {
    return ResponseEntity.ok(authService.signup(request));
  }

  @GetMapping("/login/{oAuthProvider}")
  public ResponseEntity<?> login(@PathVariable OAuthProvider oAuthProvider,
      @RequestParam String authCode, HttpServletResponse response) throws IOException {
    Optional<AuthTokens> authTokens = authService.login(oAuthProvider, authCode);
    if (authTokens.isPresent()) {
      return ResponseEntity.ok(authTokens.get());
    }

    String signupUrl = "/oauth2/auth/sign-up";
    response.sendRedirect(signupUrl);
    return new ResponseEntity<>(HttpStatus.FOUND);
  }

}
