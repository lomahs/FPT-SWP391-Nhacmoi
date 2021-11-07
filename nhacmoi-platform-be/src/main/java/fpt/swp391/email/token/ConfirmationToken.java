package fpt.swp391.email.token;

import fpt.swp391.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String resetPasswordToken;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;

    public ConfirmationToken(String token, String resetPasswordToken, LocalDateTime createAt, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.resetPasswordToken = resetPasswordToken;
        this.createAt = createAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }


}
