package jp.co.sample.emp_management.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.domain.LoginAdministrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class AdministratorService implements UserDetailsService {

	@Autowired
	private AdministratorRepository administratorRepository;

	@Autowired
	private PasswordEncoder encoder;

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param administrator 管理者情報
	 */
	public void insert(Administrator administrator) {
		administrator.setPassword(encoder.encode(administrator.getPassword()));
		System.out.println(administrator.getPassword());
		administratorRepository.insert(administrator);
	}

	/**
	 * ログインをします.
	 * 
	 * @param mailAddress メールアドレス
	 * @param password    パスワード
	 * @return 管理者情報 存在しない場合はnullが返ります
	 */
	public Administrator login(String mailAddress, String inPassword) {
		Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
		if (administrator == null) {
			return null;
		}

		if (encoder.matches(inPassword, administrator.getPassword())) {
			return administrator;
		} else {
			return null;
		}
	}

	/**
	 * 入力されたメールアドレスが既にDBに存在しているか確認する.
	 *
	 * @param inMailAddress 入力されたメールアドレス
	 * @return 存在していなければtrue、存在していればfalse
	 */
	public boolean checkDuplicationMail(String inMailAddress) {
		Administrator administrator = administratorRepository.findByMailAddress(inMailAddress);
		if (administrator == null) {
			return false;
		}

		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
		if (mailAddress == null || "".equals(mailAddress)) {
			throw new UsernameNotFoundException("メールアドレスが未入力です");
		}
		Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
		if (administrator == null) {
			throw new UsernameNotFoundException("そのメールアドレスは登録されていません");
		}

		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER")); // 権限を作っている

		return new LoginAdministrator(administrator, authorityList); // 権限と管理者情報で権限付き管理者情報を作り、返している
	}

//	@Override
//	public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
//		if (mailAddress == null || "".equals(mailAddress)) {
//			throw new UsernameNotFoundException("メールアドレスが空です");
//		}
//		Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
//		if (administrator == null) {
//			throw new UsernameNotFoundException("アカウントがDBに存在しません");
//		}
//		System.out.println("ServiceのloadUser");
//		return administrator;
//	}
}
