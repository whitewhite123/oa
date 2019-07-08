package com.msj.controller;

import com.msj.entity.*;
import com.msj.global.Contant;
import com.msj.service.ClaimVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/claim_voucher")
public class ClaimVoucherController {
    @Autowired
    private ClaimVoucherService claimVoucherService;

    //新增报销单
    @RequestMapping("/to_add")
    public String to_add(Map<String,Object> map){
        map.put("info",new ClaimVoucherInfo());
        map.put("items",Contant.getItems());
        return "claim_voucher_add";
    }
    @RequestMapping("/add")
    public String add(HttpSession session,ClaimVoucherInfo info){
        Employee employee = (Employee) session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherService.save(info.getClaimVoucher(),info.getItems());
        return "redirect:/claim_voucher/self";
    }

    //查看个人报销单
    @RequestMapping("/self")
    public String self(Map<String,Object> map,HttpSession session){
        Employee employee = (Employee)session.getAttribute("employee");
        String createSn = employee.getSn(); //根据创建人查看个人报销单
        List<ClaimVoucher> claimVoucherList = claimVoucherService.findSelf(createSn);
        map.put("list",claimVoucherList);
        return "claim_voucher_self";
    }

    //查看待处理的报销单
//    @RequestMapping("/deal")
//    public String deal(Map<String,Object> map,HttpSession session){
//        Employee employee = (Employee)session.getAttribute("employee");
//        String createSn = employee.getSn();
//        List<ClaimVoucher> claimVoucherList = claimVoucherService.findForDeal(createSn);
//        map.put("list",claimVoucherList);
//        return "claim_voucher_deal";
//    }

    @RequestMapping("/deal")
    public String deal(Map<String,Object> map,HttpSession session){
        Employee employee = (Employee)session.getAttribute(Contant.CURRENTUSER);
        String post = employee.getPost();
        String createSn = employee.getSn();
        if("总经理".equals(post)){
            List<ClaimVoucher> claimVoucherList = claimVoucherService.selectClaimVoucherByPrice(5000, 10000);
            System.out.println("总经理"+claimVoucherList);
            map.put("list",claimVoucherList);
        }else if("财务经理".equals(post)){
            List<ClaimVoucher> claimVoucherList = claimVoucherService.selectClaimVoucherByPrice(0,5000);
            System.out.println("财务经理"+claimVoucherList);
            map.put("list",claimVoucherList);
        }else{
            List<ClaimVoucher> claimVoucherList = claimVoucherService.findForDeal(createSn);
            map.put("list",claimVoucherList);
        }
        return "claim_voucher_deal";
    }

    //查看报销单的详情
    @RequestMapping("/detail")
    public String detail(Integer id,Map<String,Object> map){
        ClaimVoucher claimVoucher = claimVoucherService.findclaimVoucher(id);
        List<ClaimVoucherItem> items = claimVoucherService.findItems(id);
//        List<DealRecord> records = claimVoucherService.findRecords(id);
        map.put("items",items);
        map.put("claimVoucher",claimVoucher);
        return "claim_voucher_detail";
    }

    //修改报销单
    @RequestMapping("/to_update")
    public String to_update(Integer id,Map<String,Object> map){
        map.put("items",Contant.getItems());
        ClaimVoucherInfo info = new ClaimVoucherInfo();
        info.setClaimVoucher(claimVoucherService.findclaimVoucher(id));
        info.setItems(claimVoucherService.findItems(id));
        map.put("info",info);
        return "claim_voucher_update";
    }

    @RequestMapping("/update")
    public String update(ClaimVoucherInfo info,HttpSession session){
        Employee employee = (Employee)session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherService.edit(info.getClaimVoucher(),info.getItems());
        return "redirect:/claim_voucher/deal";
    }

    //提交报销单
    @RequestMapping("/submit")
    public String submit(Integer id){
        claimVoucherService.submit(id);
        return "redirect:/claim_voucher/deal";
    }

}
