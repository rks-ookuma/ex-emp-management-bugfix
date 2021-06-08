package jp.co.sample.emp_management.form;

/**
 * 従業員を名前で検索する際に利用されるフォーム.
 *
 * @author takahiro.okuma
 *
 */
public class SerchEmployeeByNameForm {

	/** 検索したい名前 */
	private String serchName;

	@Override
	public String toString() {
		return "SerchEmployeeByNameForm [serchName=" + serchName + "]";
	}

	public String getSerchName() {
		return serchName;
	}

	public void setSerchName(String serchName) {
		this.serchName = serchName;
	}

}
