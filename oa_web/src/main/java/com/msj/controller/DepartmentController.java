package com.msj.controller;

import com.msj.entity.Department;
import com.msj.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //查询全部
    @RequestMapping("/list")
    public String list(Map<String,Object> map){
        List<Department> departmentList = departmentService.findAll();
        map.put("list",departmentList);
        return "department_list";
    }

    //新增
    @RequestMapping("/to_add")
    public String to_add(Map<String,Object> map){
        map.put("department",new Department());
        return "department_add";
    }
    @RequestMapping("/add")
    public String add(Department department){
        departmentService.insertOne(department);
        return "redirect:/department/list";
    }

    //删除
    @RequestMapping("/remove")
    public String remove(String sn){
        departmentService.deleteOne(sn);
        return "redirect:/department/list";
    }

    //更新
    @RequestMapping("/to_update")
    public String to_update(Map<String,Object> map,String sn){
        Department department = departmentService.selectOne(sn);
        map.put("department",department);
        return "department_update";
    }
    @RequestMapping("/update")
    public String update(Department department){
        departmentService.updateOne(department);
        return "redirect:/department/list";
    }


}
