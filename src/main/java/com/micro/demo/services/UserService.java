package com.micro.demo.services;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.micro.demo.SpringStaterApplication;
import com.micro.demo.models.User;
import com.micro.demo.repositories.UserRepository;
import com.mysql.jdbc.Security;

@Service
public class UserService implements UserDetailsService {

	@Value("${app.user.verification}")
	private boolean requireActivation;

	@Value("${app.secret}")
	private String applicationSecret;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HttpSession httpSession;

	public final String CURRENT_USER_KEY = "CURRENT_USER ";

	public UserDetails loadUserBYname(String username) throws UsernameNotFoundException {
		User user = userRepository.findOneByUserNameOrEmail(username, username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		if (requireActivation && !user.getToken().equals("1")) {
			// SpringStaterApplication.log.
			throw new UsernameNotFoundException(username + "has not been activated yet");
		}
		httpSession.setAttribute(CURRENT_USER_KEY, user);
		List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), auth);
	}

	public void autologin(User user) {
		autologin(user.getUserName());
	}

	public void autologin(String username) {
		UserDetails userDetails = this.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
		if (auth.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
	}

	public User register(User user) {
		user.setPassword(encodeUserPassword(user.getPassword()));
		if (this.userRepository.findOneByUserName(user.getUserName())==null && this.userRepository.findOneByEmail(user.getEmail())==null) {
			String activation = createActivationToken(user, false);
			user.setToken(activation);
			this.userRepository.save(user);
			return user;
		}
		return null;
	}

	private String encodeUserPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}

	public boolean delete(Long id) {
		this.userRepository.deleteById(id);
		return true;
	}

	public User activate(String activation) {
		if (activation.equals("1") || activation.length() < 5) {
			return null;
		}
		User u = this.userRepository.findOneByToken(activation);
		if (u != null) {
			u.setToken("1");
			this.userRepository.save(u);
			return u;
		}
		return null;
	}

	public String createActivationToken(User user, boolean save) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		String activationToken = encoder.encodePassword(user.getUserName(), applicationSecret);
		if (save) {
			user.setToken(activationToken);
			this.userRepository.save(user);
		}
		return activationToken;
	}
	public String createResetPasswordToken(User user ,boolean save) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		String resetToken = encoder.encodePassword(user.getEmail(), applicationSecret);
		if (save) {
			user.setToken(resetToken);
			this.userRepository.save(user);
		}
		return resetToken ;
	}

	public boolean restPassword(User user) {
		User u = this.userRepository.findOneByUserName(user.getUserName());
		if (u != null) {
			u.setPassword(encodeUserPassword(user.getPassword()));
			u.setToken("1");
			this.userRepository.save(u);
			return true;
		}
		return false;
	}
	
	public void updateUser(User user) {
		updateUser(user.getUserName(),user);
	}
	public void updateUser(String username,User newData) {
		this.userRepository.updateUser(username, newData.getEmail(), newData.getFirstname(), newData.getLastname(), newData.getAddress(), newData.getCompanyName());
	}
	public User getLoggedInUser(boolean forceFresh) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = (User) httpSession.getAttribute(CURRENT_USER_KEY);
		if (forceFresh || httpSession.getAttribute(CURRENT_USER_KEY)==null) {
			user = this.userRepository.findOneByUserName(userName);
			httpSession.setAttribute(CURRENT_USER_KEY, user);
		}
		return user;
	}
	public void updateLastLogin(String username) {
		this.userRepository.updateLastLogin(username);
	}
	public void updateProfilePicture(User user, String profilePicture) {
		this.userRepository.updateProfilePicture(user.getUserName(), profilePicture);
	}

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
