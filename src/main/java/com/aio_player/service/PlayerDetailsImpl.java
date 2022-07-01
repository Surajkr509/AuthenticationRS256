package com.aio_player.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aio_player.model.Players;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PlayerDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String uuid;
	private String userName;
	private String email;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private String mobNo;
	private boolean loginStatus;

	public PlayerDetailsImpl(Long id, String uuid, String userName, String email, String password,
			Collection<? extends GrantedAuthority> authorities, String mobNo, boolean loginStatus) {
		this.id = id;
		this.uuid = uuid;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.mobNo = mobNo;
		this.loginStatus = loginStatus;
	}

	public static PlayerDetailsImpl buildUserWithAuth(Players user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(user.getRoleId().getRole()));
		return new PlayerDetailsImpl(user.getId(), user.getUuid(), user.getUserName(), user.getEmail(),
				user.getPassword(), authorities, user.getMobNo(), user.isLoginStatus());
	}

	public Long getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	public String getMobNo() {
		return mobNo;
	}

	public boolean isLoginStatus() {
		return loginStatus;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PlayerDetailsImpl user = (PlayerDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
