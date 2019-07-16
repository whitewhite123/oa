package com.msj.dao;

import com.msj.entity.Employee;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;

public interface EmployeeDao {
    //查询全部员工信息
    List<Employee> selectAll();
    //插入员工信息
    void insertOne(Employee employee);
    //删除员工信息
    void deleteOne(String sn);
    //更新员工信息
    Employee selectOne(String sn);
    void updateOne(Employee employee);

    Employee selectNameByPost(String post);

}
