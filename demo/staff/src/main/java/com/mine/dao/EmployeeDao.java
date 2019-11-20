package com.mine.dao;

import com.mine.pojo.Department;
import com.mine.pojo.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EmployeeDao {

    @Autowired
    private DepartmentDao departmentDao;
    private static Map<Integer, Employee> employees = null;
    static {
        employees = new HashMap<Integer, Employee>();
        employees.put(1001,new Employee(1001,"张三","938723ds@163.com",1,new Department(101,"政教处")));
        employees.put(1002,new Employee(1002,"阿模块 ","4dfdg@163.com",0,new Department(102,"市场部")));
        employees.put(1003,new Employee(1003,"水电费","234vfdfg@163.com",1,new Department(103,"后勤处")));
        employees.put(1004,new Employee(1004,"偶回家","134dffg@163.com",0,new Department(104,"教务处")));
        employees.put(1005,new Employee(1005,"二次","0092f@163.com",1,new Department(105,"行政部")));
    }

    private static Integer initId =1006;
    public void save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(initId++);
        }
        employee.setDepartment(departmentDao.getDepartmentById(employee.getDepartment().getId()));
        employees.put(employee.getId(),employee);
    }

    public Collection<Employee> getAll() {
        return employees.values();
    }


    public Employee getEmployeeById(Integer id) {
        return employees.get(id);
    }

    public void deleteById(Integer id) {
        employees.remove(id);
    }
}
