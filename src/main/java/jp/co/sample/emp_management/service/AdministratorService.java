package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class AdministratorService {

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
}
