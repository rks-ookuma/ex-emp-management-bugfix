package jp.co.sample.emp_management.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 管理者のログイン情報を収納するエンティティ.
 *
 * @author takahiro.okuma
 *
 */
public class LoginAdministrator extends User {

	private static final long serialVersionUID = 1L;

	private final Administrator administrator;

	/**
	 * 通常の管理者情報と認可を設定するコンストラクタ.
	 * 
	 * @param administrator 管理者ドメイン
	 * @param authorityList 権限情報の入ったリスト
	 */
	public LoginAdministrator(Administrator administrator, Collection<GrantedAuthority> authorityList) {
		super(administrator.getMailAddress(), administrator.getPassword(), authorityList);
		this.administrator = administrator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Administrator getAdministrator() {
		return administrator;
	}

	// TODO: セッターが自動で生成されないのはなぜ？

}
