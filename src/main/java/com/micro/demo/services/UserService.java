package com.micro.demo.services;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.micro.demo.SpringStaterApplication;
import com.micro.demo.models.User;
import com.micro.demo.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Value("${app.user.verification}")
	private boolean requireActivation ;
	
	@Value("${app.secret}")
	private String applicationSecret ;
	
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
		//	SpringStaterApplication.log.
			throw new UsernameNotFoundException(username + "has not been activated yet");
		}
		httpSession.setAttribute(CURRENT_USER_KEY, user);
		List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), auth);
	}
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
