package fpt.swp391.service.impl;

import fpt.swp391.email.EmailSender;
import fpt.swp391.email.token.ConfirmationToken;
import fpt.swp391.email.token.ConfirmationTokenService;
import fpt.swp391.model.*;
import fpt.swp391.repository.AccountRepository;
import fpt.swp391.repository.UserRepository;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import fpt.swp391.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final EmailSender emailSender;

    private final ConfirmationTokenService confirmationTokenService;

    private final AccountRepository accountRepository;

    private UserRepository userRepository;

    private IPlaylistService playlistService;

    private ISongService songService;

    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public User getUserByAccountName(String name) {
        return userRepository.getUserByAccountName(name);
    }

    @Override
    public void deleteUserById(String id) {
        Optional<User> userOptional = getUserByID(id);

        userOptional.ifPresent(
                user -> {
                    Set<Playlist> listPlaylists = new HashSet<>(user.getListPlaylist());
                    Set<Song> listSongs = new HashSet<>(user.getListSong());

                    listPlaylists.forEach(
                            playlist -> {
                                playlist.setOwner(null);

                                playlistService.savePlaylist(playlist);
                            }
                    );

                    listSongs.forEach(
                            song -> {
                                song.setAdder(null);

                                songService.saveSong(song);
                            }
                    );
                }
        );

        userRepository.deleteById(id);
    }

    @Override
    public List<User> getListUsers() {

        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByID(String id) {

        return userRepository.findById(id);
    }

    private String createToken(){
        return UUID.randomUUID().toString();
    }

    @Override
    public User registerNewUser(User user) {
        String token = createToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );
            user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
            user.getAccount().getRoles().add(new Role("ROLE_USER"));
            User newUser = saveUser(user);

            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = "http://localhost:8080/api/user/register/confirm?token=" + token;
            emailSender.send(user.getUser_email(), EmailConfirm(user.getUser_name(), link));

            Playlist playlist = playlistService.savePlaylist(new Playlist("Liked Song", newUser));

            newUser.setListPlaylist(new HashSet<>());
            newUser.getListPlaylist().add(playlist);

            return newUser;
    }

    public ResponseEntity<LoginResponse> resendEmailConfirm(String email){
        User user = userRepository.getUserByEmail(email);
        String token = createToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/api/user/register/confirm?token=" + token;
        emailSender.send(user.getUser_email(), EmailConfirm(user.getUser_name(), link));
        return new ResponseEntity<>(new LoginResponse(null, "", "Resend Email success."), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<LoginResponse> confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));
        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmAt(token);
        userRepository.enabledAppUser(
                confirmationToken.getUser().getUser_email()
        );
        return new ResponseEntity<>(new LoginResponse(confirmationToken.getUser(), null, "User email confirm successful."), HttpStatus.OK);
    }

    public String EmailConfirm(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 5 minutes. <p>See you soon</p>  <p>NhacMoi.</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public String EmailForgot(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reset Password</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> A request has been received to reset password. Please click reset password below to rest your pass. </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Reset Password</a> </p></blockquote>\n Link will expire in 5 minutes. <p>See you soon</p>  <p>NhacMoi.</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    @Override
    public User changeRole(Optional<User> userOptional, String role) {

        return userOptional.map(user -> {
            user.getAccount().getRoles().add(new Role(role));
            return userRepository.save(user);
        }).orElse(null);
    }

    @Override
    public User changePassword(Map<String, String> jsonData) {
        String userId = jsonData.get("user_id");
        String oldPassword = jsonData.get("old_password");
        String newPassword = jsonData.get("new_password");

        String passRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$";

        Optional<User> userOptional = getUserByID(userId);

        return userOptional.map(user -> {
            if (newPassword.matches(passRegex) && passwordEncoder.matches(oldPassword, user.getAccount().getPassword())) {
                user.getAccount().setPassword(passwordEncoder.encode(newPassword));
                return saveUser(user);
            } else {
                return null;
            }
        }).orElse(null);
    }

    @Transactional
    public ResponseEntity<LoginResponse> confirmResetPassword(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getResetPasswordToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        return new ResponseEntity<>(new LoginResponse(confirmationToken.getUser(), token, "Allow user change pass."), HttpStatus.OK);
    }


    public void forgotPassword(String email){
        User user = userRepository.getUserByEmail(email);
        String resetPassword_token = createToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                "",
                resetPassword_token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/api/user/forgot/confirm?token=" + resetPassword_token;
        emailSender.send(user.getUser_email(), EmailForgot(user.getUser_name(), link));
    }

    public ResponseEntity<LoginResponse> updatePassword(String email, String passWord, String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getResetPasswordToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));
        Account account = accountRepository.getAccountByEmail(email);
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        String passRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,}$";
        if(account != null && passWord.matches(passRegex) && !expiredAt.isBefore(LocalDateTime.now())){
            account.setPassword(passwordEncoder.encode(passWord));
            accountRepository.save(account);
        } else {
            throw new IllegalStateException("Something went wrong!");
        }
        return new ResponseEntity<>(new LoginResponse(null, null, "User update password successful."), HttpStatus.OK);
    }
}