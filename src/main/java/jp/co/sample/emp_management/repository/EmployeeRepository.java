package jp.co.sample.emp_management.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.emp_management.domain.Employee;

/**
 * employeesテーブルを操作するリポジトリ.
 * 
 * @author igamasayuki
 * 
 */
@Repository
public class EmployeeRepository {

	/**
	 * Employeeオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
		Employee employee = new Employee();
		employee.setId(rs.getInt("id"));
		employee.setName(rs.getString("name"));
		employee.setImage(rs.getString("image"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hire_date"));
		employee.setMailAddress(rs.getString("mail_address"));
		employee.setZipCode(rs.getString("zip_code"));
		employee.setAddress(rs.getString("address"));
		employee.setTelephone(rs.getString("telephone"));
		employee.setSalary(rs.getInt("salary"));
		employee.setCharacteristics(rs.getString("characteristics"));
		employee.setDependentsCount(rs.getInt("dependents_count"));
		return employee;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 従業員一覧情報を入社日順で取得します.
	 * 
	 * @return 全従業員一覧 従業員が存在しない場合はサイズ0件の従業員一覧を返します
	 */
	public List<Employee> findAll() {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees"
				+ " ORDER BY hire_date DESC";

		List<Employee> developmentList = template.query(sql, EMPLOYEE_ROW_MAPPER);

		return developmentList;
	}

	/**
	 * 10件の制限付きで従業員一覧を取得する.
	 *
	 * @param startIndex 指定した番号の次から取得する
	 * @return 指定した番号から指定した件数分、入社日の降順で並んだ従業員のリスト
	 */
	public List<Employee> findLimited(int startIndex) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees"
				+ " ORDER BY hire_date DESC LIMIT 10 OFFSET :startIndex";
		SqlParameterSource param = new MapSqlParameterSource().addValue("startIndex", startIndex);
		List<Employee> employeeList = template.query(sql, param, EMPLOYEE_ROW_MAPPER);

		return employeeList;
	}

	/**
	 * 主キーから従業員情報を取得します.
	 * 
	 * @param id 検索したい従業員ID
	 * @return 検索された従業員情報
	 * @exception org.springframework.dao.DataAccessException 従業員が存在しない場合は例外を発生します
	 */
	public Employee load(Integer id) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE id=:id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Employee development = template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);

		return development;
	}

	/**
	 * 従業員情報を変更します.
	 */
	public void update(Employee employee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);

		String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
		template.update(updateSql, param);
	}

	/**
	 * 従業員を名前で10件分曖昧検索する.
	 *
	 * @param name       検索したい名前
	 * @param startIndex 取得を開始する番号の前の番号
	 * @return 指定した名前が含まれる従業員のリスト
	 */
	public List<Employee> findLimitedByLikeName(String name, int startIndex) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees "
				+ " WHERE name LIKE :name ORDER BY hire_date DESC LIMIT 10 OFFSET :startIndex;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("startIndex",
				startIndex);
		List<Employee> employeeList = template.query(sql, param, EMPLOYEE_ROW_MAPPER);
		return employeeList;
	}

	/**
	 * 従業員を名前であいまい検索する.
	 *
	 * @param name 検索したい名前
	 * @return 指定した名前が含まれる従業員全員が入ったリスト
	 */
	public List<Employee> findByLikeName(String name) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees "
				+ " WHERE name LIKE :name ORDER BY hire_date DESC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Employee> employeeList = template.query(sql, param, EMPLOYEE_ROW_MAPPER);
		return employeeList;

	}

	/**
	 * 従業員を新規に登録する.
	 *
	 * @param employee 登録したい従業員ドメイン
	 */
	public void insert(Employee employee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
		String sql = "INSERT INTO employees (id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count)"
				+ " VALUES(:id,:name,:image,:gender,:hireDate,:mailAddress,:zipCode,:address,:telephone,:salary,:characteristics,:dependentsCount);";
		template.update(sql, param);
	}

	/**
	 * 登録されている最後のIDを取得する.
	 *
	 * @return 登録されている最後のID
	 */
	public Integer getMaxId() {
		String sql = "SELECT id FROM employees WHERE id=MAX(id);";
		SqlParameterSource param = new MapSqlParameterSource();
		Integer maxId = template.queryForObject(sql, param, Integer.class);
		return maxId;
	}
}
