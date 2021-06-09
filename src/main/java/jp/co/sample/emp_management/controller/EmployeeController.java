package jp.co.sample.emp_management.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.InsertEmployeeForm;
import jp.co.sample.emp_management.form.SerchEmployeeByNameForm;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	@ModelAttribute
	public SerchEmployeeByNameForm setUpSerchEmployeeByNameForm() {
		return new SerchEmployeeByNameForm();
	}

	@ModelAttribute
	public InsertEmployeeForm setupInsertEmployeeForm() {
		return new InsertEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/showList")
	public String showList(Model model, Integer selectPage, SerchEmployeeByNameForm serchEmployeeByNameForm) {
		if (selectPage == null) {
			selectPage = 1;
		}

		int pageLimit = 0;
		if (serchEmployeeByNameForm.getSerchName() == null) {
			pageLimit = employeeService.getPageLimit();
		} else {
			pageLimit = employeeService.getPageLimit(serchEmployeeByNameForm.getSerchName());
		}

		List<Employee> employeeList = employeeService.showList(selectPage, serchEmployeeByNameForm.getSerchName());
		if (employeeList.size() == 0) {
			model.addAttribute("notExistEmployee", "検索結果が１件もありませんでした");
			employeeList = employeeService.showList(selectPage, null);
			pageLimit = employeeService.getPageLimit();
		}
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("selectPage", selectPage);
		model.addAttribute("pageLimit", pageLimit);

		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	/**
	 * 従業員登録画面を表示する.
	 *
	 * @return 従業員登録画面
	 */
	@RequestMapping("/showEmployeeRegister")
	public String showEmployeeRegister() {
		return "employee/insert";
	}

	/**
	 * 従業員を新規登録する.
	 *
	 * @param insertEmployeeForm 従業員を新規登録する際に利用されるフォーム
	 * @param result             入力値チェックのエラー群
	 * @return 登録できれば従業員一覧画面、失敗すれば従業員登録画面
	 */
	@RequestMapping("/registerEmployee")
	public String registerEmployee(@Validated InsertEmployeeForm insertEmployeeForm, BindingResult result) {
		if (result.hasErrors()) {
			return "employee/insert";
		}

		Employee employee = new Employee();
		BeanUtils.copyProperties(insertEmployeeForm, employee);
		employee.setHireDate(Date.valueOf(insertEmployeeForm.getHireDate()));
		employee.setImage(insertEmployeeForm.getImage().getOriginalFilename());

		String path = EmployeeController.class.getResource("/static").getFile() + "/img";
		path = path.substring(1);
		System.out.println(path);
		try {
			Files.copy(insertEmployeeForm.getImage().getInputStream(),
					Paths.get(path, insertEmployeeForm.getImage().getOriginalFilename()));
			// insertEmployeeForm.getImage().transferTo(new
			// File("../../../../../../resources/static/img/" + employee.getImage()));
		} catch (Exception e) {
			e.printStackTrace();
			result.rejectValue("image", "xxxxx", new Object[] { 50000 }, "ファイルのアップロードに失敗しました。もう一度お試しください。");
			return "employee/insert";
		}
		System.out.println("登録成功");
		System.out.println("employee : " + employee);

		return "redirect:/employee/showList";
	}
}
