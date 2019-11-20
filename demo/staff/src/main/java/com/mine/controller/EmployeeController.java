package com.mine.controller;

import com.mine.dao.DepartmentDao;
import com.mine.dao.EmployeeDao;
import com.mine.pojo.Department;
import com.mine.pojo.Employee;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;
    @RequestMapping("/emp")
    public String List(Model model) {
        Collection<Employee> employees = employeeDao.getAll();
        model.addAttribute("emps",employees);
        return "emp/list";
    }

    @GetMapping("/add")
    public String toAddpage(Model model) {
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments",departments);
        return "emp/add";
    }

    @PostMapping("/add")
    public String addEmp(Employee employee) {
        employeeDao.save(employee);
         return "redirect:/emp";
    }
//      编辑
    @GetMapping("/emp/{id}")
    public String updateEmp(@PathVariable("id")Integer id, Model model) {
        Employee employee = employeeDao.getEmployeeById(id);
        model.addAttribute("emp", employee);
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments",departments);
        return "emp/update";
    }

    @PostMapping("/updateEmp")
    public String updateChanged(Employee employee) {
        employeeDao.save(employee);
        return "redirect:/emp";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmp(@PathVariable("id") Integer id) {
        employeeDao.deleteById(id);
        return "redirect:/emp";
    }
}
