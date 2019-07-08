package com.msj.service.impl;
import com.msj.dao.LoginDao;
import com.msj.entity.Employee;
import com.msj.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private LoginDao loginDao;

    public Employee selectOne(String sn, String password) {
        Employee employee = loginDao.selectOne(sn);
        //判断取出来的employee是否为空，密码是否一致
        if(employee!=null&&employee.getPassword().equals(password)){
            return employee;
        }
        return null;
    }


    public void changePassword(Employee employee) {
        loginDao.changePassword(employee);
    }


}
