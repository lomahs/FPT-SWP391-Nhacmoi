package fpt.swp391.email.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
    }

    public Optional<ConfirmationToken> getResetPasswordToken(String token){
        return confirmationTokenRepository.findByResetPasswordToken(token);
    }

    public void setConfirmAt(String token) {
        confirmationTokenRepository.updateConfirmAt(
                token, LocalDateTime.now()
        );
    }

    public void updateConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.updateConfirmToken(token.getToken(), token.getCreateAt(), token.getExpiresAt());
    }
}