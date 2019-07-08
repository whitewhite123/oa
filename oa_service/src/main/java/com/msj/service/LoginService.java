package com.msj.service;

import com.msj.entity.Employee;
import org.apache.ibatis.annotations.Param;

public interface LoginService {
    //多个参数，得用@Param
    Employee selectOne(@Param("sn")String sn, @Param("password")String password);
    void changePassword(Employee employee);
}
