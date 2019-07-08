package com.msj.service;

import com.msj.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EmployeeService {
    List<Employee> findAll();
    void insertOne(Employee employee);
    void deleteOne(String sn);
    Employee selectOne(String sn);
    void update(Employee employee);

}
