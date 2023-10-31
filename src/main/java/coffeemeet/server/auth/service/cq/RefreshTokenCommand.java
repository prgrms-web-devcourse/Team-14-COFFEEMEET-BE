package coffeemeet.server.auth.service.cq;

import coffeemeet.server.auth.domain.RefreshToken;
import coffeemeet.server.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenCommand {

  private final RefreshTokenRepository refreshTokenRepository;

  public void createRefreshToken(RefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

  public void deleteRefreshToken(Long userId) {
    refreshTokenRepository.deleteById(userId);
  }

}
