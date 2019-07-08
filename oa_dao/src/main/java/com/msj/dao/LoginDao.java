package com.msj.dao;

import com.msj.entity.Employee;
import org.apache.ibatis.annotations.Param;

public interface LoginDao {
    Employee selectOne(String sn);
    void changePassword(Employee employee);
}
