package com.msj.controller;
import com.msj.entity.Employee;
import com.msj.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    //跳转登录页面
    @RequestMapping("/to_login")
    public String to_login(){
        return "login";
    }

    //登录处理
    @RequestMapping("/login")
    public String login(String sn, String password, HttpSession session){
        Employee employee = loginService.selectOne(sn, password);;
        //查询出来，就可以登录成功
        if(employee!=null){
            //登录成功，设置session
            session.setAttribute("employee",employee);
            return "self";
        }else{
            return "redirect:/to_login";
        }
    }

    //修改密码
    @RequestMapping("/to_change_password")
    public String to_changePassword(){
        return "change_password";
    }
    @RequestMapping("/change_password")
    public String change_password(HttpSession session,String old,String new1,String new2){
        Employee employee = (Employee)session.getAttribute("employee");
        if((employee.getPassword()).equals(old)){
            if(new1.equals(new2)){
                employee.setPassword(new1);
                //里面的employee要从session.getAttribute取出来的
                loginService.changePassword(employee);
                return "redirect:/to_login";
            }
        }
        return "redirect:/to_change_password";
    }
    //查询个人信息
    @RequestMapping("/self")
    public String self(){
        return "self";
    }

    //退出登录
    @RequestMapping("/quit")
    public String quit(HttpSession session){
        session.setAttribute("employee",null);
        return "redirect:to_login";
    }
}
