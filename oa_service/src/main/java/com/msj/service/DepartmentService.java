package com.msj.service;
import com.msj.entity.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
    void insertOne(Department department);
    void deleteOne(String sn);
    Department selectOne(String sn);
    void updateOne(Department department);
}
