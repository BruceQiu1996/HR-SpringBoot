package org.sang.controller.salary;

import org.sang.bean.Department;
import org.sang.bean.Employee;
import org.sang.service.DepartmentService;
import org.sang.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.util.List;

/**
 * Created by sang on 2018/1/26.
 */
@RestController
@RequestMapping("/salary/table")
public class SalaryTableController {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    SalaryService salaryService;
    @RequestMapping("/deps")
    public List<Department> departments() {
        return departmentService.getAllDeps();
    }
    @RequestMapping(value = "/salaryByDep/{id}",method=RequestMethod.GET)
    public List<Employee> salaryByDep(@PathVariable Integer id){
    	return salaryService.selSalaryByDep(id);
    }
}
