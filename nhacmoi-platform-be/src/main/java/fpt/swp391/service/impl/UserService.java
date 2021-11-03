package fpt.swp391.service.impl;

import fpt.swp391.email.EmailSender;
import fpt.swp391.email.token.ConfirmationToken;
import fpt.swp391.email.token.ConfirmationTokenService;
import fpt.swp391.model.Playlist;
import fpt.swp391.model.Role;
import fpt.swp391.model.Song;
import fpt.swp391.model.User;
import fpt.swp391.repository.UserRepository;
import fpt.swp391.service.IPlaylistService;
import fpt.swp391.service.ISongService;
import fpt.swp391.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IPlaylistService playlistService;

    @Autowired
    private ISongService songService;

    @Autowired
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

    @Override
    public User registerNewUser(User user) {
        user.getAccount().setPassword(passwordEncoder.encode(user.getAccount().getPassword()));
        user.getAccount().getRoles().add(new Role("ROLE_USER"));

        User newUser = saveUser(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/api/user/register/confirm?token=" + token;
        emailSender.send(user.getUser_email(), buildEmail(user.getUser_name(), link));


        Playlist playlist = playlistService.savePlaylist(new Playlist("Liked Song", newUser));

        newUser.setListPlaylist(new HashSet<>());
        newUser.getListPlaylist().add(playlist);

        return newUser;
    }

    @Transactional
    public String confirmToken(String token) {
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
        return "confirmed";
    }

    public String buildEmail(String name, String link) {
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

    public int enableAppUser(String email) {
        return userRepository.enabledAppUser(email);
    }
}