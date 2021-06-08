package jp.co.sample.emp_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * 従業員をページング形式で10件取得する.
	 *
	 * @param selectPage 指定したページ
	 * @return 指定したページにおける従業員が入社日の降順に入ったリスト
	 */
	public List<Employee> showList(int selectPage) {

		int displayCount = 10;
		// startIndexの次から取得する
		// 1が指定されたらstartIndexは０、2が指定されたらStartIndexは10から
		int startIndex = selectPage * displayCount - displayCount;
		List<Employee> employeeList = employeeRepository.findLimited(startIndex);
		return employeeList;
	}

	/**
	 * ページの上限を取得する.
	 *
	 * @return ページの上限
	 */
	public Integer getPageLimit() {
		List<Employee> employeeList = employeeRepository.findAll();
		int pageLimit = (int) Math.ceil(employeeList.size() / 10.0);
		return pageLimit;
	}

	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws org.springframework.dao.DataAccessException 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}

	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee 更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}
}
