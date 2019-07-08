package com.msj.service.impl;
import com.msj.dao.EmployeeDao;
import com.msj.entity.Employee;
import com.msj.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private EmployeeDao employeeDao;


    public List<Employee> findAll() {
        return employeeDao.selectAll();
    }

    public void insertOne(Employee employee) {
        employeeDao.insertOne(employee);
    }

    public void deleteOne(String sn) {
        employeeDao.deleteOne(sn);
    }

    public Employee selectOne(String sn) {
        return employeeDao.selectOne(sn);
    }

    public void update(Employee employee) {
        employeeDao.updateOne(employee);
    }


}
