package com.msj.service.impl;

import com.msj.dao.DepartmentDao;
import com.msj.entity.Department;
import com.msj.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{
    @Autowired
    private DepartmentDao departmentDao;

    public List<Department> findAll() {
        return departmentDao.selectAll();
    }

    public void insertOne(Department department) {
        departmentDao.insertOne(department);
    }

    public void deleteOne(String sn) {
        departmentDao.deleteOne(sn);
    }

    public Department selectOne(String sn) {
        return departmentDao.selectOne(sn);
    }

    public void updateOne(Department department) {
        departmentDao.updateOne(department);
    }
}
