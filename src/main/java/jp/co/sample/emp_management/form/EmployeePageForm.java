package jp.co.sample.emp_management.form;

/**
 * 従業員一覧においてページを移動するために利用されるフォーム.
 *
 * @author takahiro.okuma
 *
 */
public class EmployeePageForm {

	/** 移動したい指定されたページ */
	private Integer selectPage;

	public Integer getSelectPage() {
		return selectPage;
	}

	public void setSelectPage(Integer selectPage) {
		this.selectPage = selectPage;
	}

	@Override
	public String toString() {
		return "EmployeePageForm [selectPage=" + selectPage + "]";
	}

}
