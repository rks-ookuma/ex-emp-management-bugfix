package jp.co.sample.emp_management.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

/**
 * 従業員を新規登録する際に利用されるフォーム.
 *
 * @author takahiro.okuma
 *
 */
public class InsertEmployeeForm {

	/** 従業員名 */
	@NotBlank(message = "名前を入力してください")
	private String name;
	/** 画像 */
	private MultipartFile image;
	/** 性別 */
	@NotEmpty(message = "性別を選択してください")
	private String gender;
	/** 入社日 */
	@Pattern(regexp = "^[0-9] {4}-[0-9] {2}-[0-9] {2}$", message = "入社日を選択してください")
	private String hireDate;
	/** メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	private String mailAddress;
	/** 郵便番号 */
	@Size(min = 1, max = 7, message = "郵便番号をハイフンなしの７桁で入力してください")
	private String zipCode;
	/** 住所 */
	@NotBlank(message = "住所を入力してください")
	private String address;
	/** 電話番号 */
	@Size(min = 1, message = "電話番号を入力してください")
	private String telephone;
	/** 給料 */
	@NotNull(message = "給料を入力してください")
	private Integer salary;
	/** 特性 */
	@NotBlank(message = "特性を入力してください")
	private String characteristics;
	/** 扶養人数 */
	@NotNull(message = "扶養人数を入力してください")
	private Integer dependentsCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	public Integer getDependentsCount() {
		return dependentsCount;
	}

	public void setDependentsCount(Integer dependentsCount) {
		this.dependentsCount = dependentsCount;
	}

	@Override
	public String toString() {
		return "InsertEmployeeForm [name=" + name + ", image=" + image + ", gender=" + gender + ", hireDate=" + hireDate
				+ ", mailAddress=" + mailAddress + ", zipCode=" + zipCode + ", address=" + address + ", telephone="
				+ telephone + ", salary=" + salary + ", characteristics=" + characteristics + ", dependentsCount="
				+ dependentsCount + "]";
	}

}
