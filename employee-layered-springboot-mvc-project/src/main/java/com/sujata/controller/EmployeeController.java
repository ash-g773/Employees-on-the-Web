package com.sujata.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sujata.entity.Employee;
import com.sujata.entity.EmployeePaySlip;
import com.sujata.model.service.EmployeeService;

import lombok.Data;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping("/")
	public ModelAndView menuPageController() {
		return new ModelAndView("index");
	}
	@RequestMapping("/deleteEmpPage")
	public ModelAndView deletePageController() {
		return new ModelAndView("InputIdForDelete");
	}
	
	@RequestMapping("/deleteEmployee")
	public ModelAndView deleteEmployeeController(HttpServletRequest request) {
		ModelAndView modelAndView=new ModelAndView();
		int eId=Integer.parseInt(request.getParameter("empId"));
		String message=null;
		if(employeeService.deleteEmployee(eId))
			message="Employee Deleted with ID "+eId;
		else
			message="Employee with ID "+eId+" doesnot exist";
		
		modelAndView.addObject("message", message);
		modelAndView.setViewName("Output");
		
		return modelAndView;
	}
	
	@RequestMapping("/incrementSalaryPage")
	public ModelAndView incrementPageController() {
		return new ModelAndView("InputIdForIncrement");
	}
	
	@RequestMapping("/incrementSalary")
	public ModelAndView incrementSalaryController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		int eId = Integer.parseInt(request.getParameter("empId"));
		double salInc = Double.parseDouble(request.getParameter("salInc"));
		String message = null;
		
		if(employeeService.incrementSalary(eId, salInc))
			message = "Employee with ID "+eId+" salary incremented by "+salInc;
		else
			message = "Emplpoye with ID " + eId + " does not exist!";
		
		modelAndView.addObject("message", message);
		modelAndView.setViewName("Output");
		
		return modelAndView;
	}
	
	@RequestMapping("/showAll")
	public ModelAndView getAllEmployeesController() {
		ModelAndView modelAndView = new ModelAndView();
		Collection<Employee> employees = employeeService.getAllEmployees();
		
		modelAndView.addObject("employees", employees);
		modelAndView.setViewName("ShowAllEmployees");
		
		return modelAndView;
	}
	
	@RequestMapping("/searchEmployeeByIdInputPage")
	public ModelAndView searchEmployeeByIdInputPageController() {
		return new ModelAndView("InputIdForSearch");
	}
	
	@RequestMapping("/searchEmployeeById")
	public ModelAndView searchEmployeeController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		int eId = Integer.parseInt(request.getParameter("empId"));
		
		Employee emp = employeeService.searchEmployeeById(eId);
		
		if (emp != null) {
			modelAndView.addObject("employee", emp);
			modelAndView.setViewName("ShowEmployee");
		}
		else {
			modelAndView.addObject("message", "Employee with ID " + eId + " does not exist");
			modelAndView.setViewName("Output");
		}
		
		return modelAndView;
	}
	
	@RequestMapping("/searchEmployeeByDepartmentInputPage")
	public ModelAndView searchEmployeeByDepartmentPageController() {
		return new ModelAndView("InputDepartmentForSearch");
	}
	
	@RequestMapping("/searchEmployeeByDepartment") 
	public ModelAndView searchEmployeeByDepartmentController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String department = request.getParameter("department");
		
		List<Employee> emps = employeeService.getEmployeesByDepartment(department);
		
		if (emps.size() > 0) {
			modelAndView.addObject("employees", emps);
			modelAndView.setViewName("ShowAllEmployees");
		}
		else {
			modelAndView.addObject("message", "Department " + department + " does not exist, or no one works there!");
			modelAndView.setViewName("Output");
		}
		
		return modelAndView;
	}
	
	@RequestMapping("/addNewEmployeeInputPage")
	public ModelAndView addNewEmployeePageController() {
		return new ModelAndView("AddNewEmployee");
	}
	
	@RequestMapping("/addEmployee")
	public ModelAndView addEmployeeController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		Employee empAdding = new Employee();
		
		empAdding.setEmpId(Integer.parseInt(request.getParameter("empId")));
		empAdding.setEmpName(request.getParameter("empName"));
		empAdding.setEmpDesignation(request.getParameter("empDes"));
		empAdding.setEmpDepartment(request.getParameter("empDep"));
		empAdding.setEmpSalary(Double.parseDouble(request.getParameter("empSal")));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d"); //setting date format
		String empDoj = request.getParameter("empDoj");
		
		LocalDate empDojDate = LocalDate.parse(empDoj, formatter); //creates a new localdate object from string
		empAdding.setDateOfJoining(empDojDate);
		
		if (employeeService.addEmployee(empAdding)) {
			modelAndView.addObject("message", "Employee added!");
			modelAndView.setViewName("Output");
		} else {
			modelAndView.addObject("message", "Employee with id " + request.getParameter("empId") + " already exists!");
			modelAndView.setViewName("Output");
		}
		
		return modelAndView;

	}
	
	@RequestMapping("/searchEmployeeByDesignationInputPage")
	public ModelAndView searchEmployeeByDesignationInputPageController() {
		return new ModelAndView("InputDesignationForSearch");
	}
	
	@RequestMapping("/searchEmployeeByDesignation") 
	public ModelAndView searchEmployeeByDesignationController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String des = request.getParameter("designation");
		
		List<Employee> emps = employeeService.searchByDesignation(des);
		
		if (emps.size() > 0) {
			modelAndView.addObject("employees", emps);
			modelAndView.setViewName("ShowAllEmployees");
		}
		else {
			modelAndView.addObject("message", "Designation " + des + " does not exist, or no one has that role!");
			modelAndView.setViewName("Output");
		}
		
		return modelAndView;
	}
	
	@RequestMapping("/deleteEmpByNameInputPage")
	public ModelAndView deleteEmpByNameInputPageController() {
		return new ModelAndView("InputNameforDelete");
	}
	
	@RequestMapping("/deleteEmployeeByName")
	public ModelAndView deleteEmpByNameController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		String empName = request.getParameter("name");
		
		if(employeeService.deleteEmployeeByName(empName)) {
			modelAndView.addObject("message", "Employee deleted!");
			modelAndView.setViewName("Output");
		} else {
			modelAndView.addObject("message", "Employee " + empName + " does not exist");
			modelAndView.setViewName("Output");
		}
		
		return modelAndView;
			
	}
	
	@RequestMapping("/generatePayslipInputPage")
	public ModelAndView generatePayslipInputPageController() {
		return new ModelAndView("InputIdForSearchGenPaySlip");
	}
		
	@RequestMapping("/searchEmployeeByIdAndGeneratePayslip")
	public ModelAndView searchEmployeeAndGeneratePayslipController(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		int eId = Integer.parseInt(request.getParameter("empId"));
		
		EmployeePaySlip empPay = employeeService.generatePaySlip(eId);
		
		if (empPay != null) {
			modelAndView.addObject("employeePayslip", empPay);
			modelAndView.setViewName("ShowEmployeePayslip");
		}
		else {
			modelAndView.addObject("message", "Employee with ID " + eId + " does not exist");
			modelAndView.setViewName("Output");
		}
		
		return modelAndView;
	}
	
}
