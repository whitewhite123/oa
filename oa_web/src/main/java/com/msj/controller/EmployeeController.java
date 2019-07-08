package com.msj.controller;

import com.msj.dao.EmployeeDao;
import com.msj.entity.Employee;
import com.msj.global.Contant;
import com.msj.service.DepartmentService;
import com.msj.service.EmployeeService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employee")
public class EmployeeController{

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;



    //查询全部
    @RequestMapping("/list")
    public String list(Map<String,Object> map){
        List<Employee> employeeList = employeeService.findAll();
        System.out.println(employeeList);
        map.put("list",employeeList);
        return "employee_list";
    }

    //新增
    @RequestMapping("/to_add")
    public String to_add(Map<String,Object> map){
        map.put("employee",new Employee());
        //所在部门
        map.put("dlist",departmentService.findAll());
        //职位
        map.put("plist",Contant.getPosts());
        return "employee_add";
    }
    @RequestMapping("/add")
    public String add(Employee employee){
        employeeService.insertOne(employee);
        return "redirect:/employee/list";
    }

    //删除
    @RequestMapping("/remove")
    public String remove(String sn){
        employeeService.deleteOne(sn);
        return "redirect:/employee/list";
    }

    //更新
    @RequestMapping("/to_update")
    public String to_update(Map<String,Object> map,String sn){
        Employee employee = employeeService.selectOne(sn);
        map.put("employee",employee);
        map.put("dlist",departmentService.findAll());
        map.put("plist",Contant.getPosts());
        return "employee_update";
    }
    @RequestMapping("/update")
    public String update(Employee employee){
        employeeService.update(employee);
        return "redirect:/employee/list";
    }


}
