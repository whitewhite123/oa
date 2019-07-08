package com.msj.dao;
import com.msj.entity.Department;
import java.util.List;

public interface DepartmentDao {
    List<Department> selectAll();
    void insertOne(Department department);
    void deleteOne(String sn);
    Department selectOne(String sn);
    void updateOne(Department department);
}
