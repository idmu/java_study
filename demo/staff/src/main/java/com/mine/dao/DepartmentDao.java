package com.mine.dao;

import com.mine.pojo.Department;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DepartmentDao {
    private static Map<Integer, Department> departments = null;
    static {
        departments = new HashMap<Integer, Department>();
        departments.put(101, new Department(101,"政教处"));
        departments.put(102, new Department(102,"市场部"));
        departments.put(103, new Department(103, "后勤部"));
        departments.put(104, new Department(104, "教务处"));
        departments.put(105, new Department(105, "行政部"));
        departments.put(106, new Department(106, "财务部"));
    }

    public Collection<Department> getDepartments() {
        return departments.values();
    }

    public Department getDepartmentById(Integer id) {
        return  departments.get(id);
    }
}
