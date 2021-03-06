package com.msj.service.impl;

import com.msj.dao.ClaimVoucherDao;
import com.msj.dao.ClaimVoucherItemDao;
import com.msj.dao.DealRecordDao;
import com.msj.dao.EmployeeDao;
import com.msj.entity.ClaimVoucher;
import com.msj.entity.ClaimVoucherItem;
import com.msj.entity.DealRecord;
import com.msj.entity.Employee;
import com.msj.global.Contant;
import com.msj.service.ClaimVoucherService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.net.httpserver.HttpServerImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ClaimVoucherServiceImpl implements ClaimVoucherService{
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Autowired
    private DealRecordDao dealRecordDao;
    @Autowired
    private EmployeeDao employeeDao;

    //添加报销单
    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        Date date = new Date();
        claimVoucher.setCreateTime(date); //创建时间
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED); //状态
        //添加到claim_voucher表中
        claimVoucherDao.insertOne(claimVoucher);
        //添加到claim_voucher_item表中
        for(ClaimVoucherItem item:items){
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insertOne(item);
        }

        //添加记录到deal_record表中
        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealTime(date);
        dealRecord.setDealSn(claimVoucher.getCreateSn());
        dealRecord.setClaimVoucherId(claimVoucher.getId());
        dealRecord.setDealWay(Contant.DEAL_CREATE);
        dealRecordDao.insertOne(dealRecord);
    }

    //查询个人报销单（方法一）
//    public List<ClaimVoucher> findSelf(String createSn) {
////        List<ClaimVoucher> claimVoucherList = claimVoucherDao.selectClaimVoucherStatus(createSn);
////        Iterator<ClaimVoucher> iterator = claimVoucherList.iterator();
////        while(iterator.hasNext()){
////            ClaimVoucher next = iterator.next();
////            if((next.getStatus().equals(Contant.CLAIMVOUCHER_CREATED))){
////                //找到status = '新创建'就把它移出到list之外
////                iterator.remove();
////            }
////        }
////        return claimVoucherList;
////    }
    //查询个人报销单（方法二）
    public List<ClaimVoucher> findSelf(String createSn) {
        List<ClaimVoucher> claimVoucherList = claimVoucherDao.selectClaimVoucherStatus(createSn);
        ArrayList<ClaimVoucher> list = new ArrayList<ClaimVoucher>();
        for(ClaimVoucher c:claimVoucherList){
            if((c.getStatus()).equals(Contant.CLAIMVOUCHER_CREATED)){
                //如果报销单的status='新创建',就跳过这一个，继续下一个
                continue;
            }
            list.add(c);
        }
        System.out.println(list);
        return list;
    }

    //查询待处理的报销单
    //根据职位确定待处理的报销单
    /*
        员工：处理--新创建、已打回、已拒绝
        部门经理：处理--已提交、已修改
        总经理：处理--待复审
        财务经理：处理--已审核

     */
    public List<ClaimVoucher> findForDeal(String post) {
        ArrayList<String> statusList = new ArrayList<String>();
        if(post.equals("总经理")){
            statusList.add(Contant.CLAIMVOUCHER_RECHECK);
        }else if(post.equals("部门经理")){
            statusList.add(Contant.CLAIMVOUCHER_SUBMIT);//已提交
        }else if(post.equals("财务经理")){
            statusList.add(Contant.CLAIMVOUCHER_APPROVED);//已审核
        }else{
            statusList.add(Contant.CLAIMVOUCHER_CREATED);//新创建
            statusList.add(Contant.CLAIMVOUCHER_BACK);//已打回
            statusList.add(Contant.CLAIMVOUCHER_TERMINATED);//已拒绝
        }
        return claimVoucherDao.selectForDeal(statusList);
    }

    //查询报销单详情
    public ClaimVoucher findclaimVoucher(Integer id) {
        ClaimVoucher claimVoucher = claimVoucherDao.selectclaimVoucher(id);
        String status = claimVoucher.getStatus();
        if(status.equals(Contant.CLAIMVOUCHER_CREATED)||status.equals(Contant.CLAIMVOUCHER_BACK)){
            //如果status是新创建或者已打回，那么处理人就是自己
            claimVoucher.setDealer(claimVoucher.getCreater());
        }else if(status.equals(Contant.CLAIMVOUCHER_SUBMIT)){
            //如果status是已提交，那么处理人就是部门经理
            String post = Contant.POST_FM;//部门经理
            Employee e = employeeDao.selectNameByPost(post);
            claimVoucher.setDealer(e);
        }else if(status.equals(Contant.CLAIMVOUCHER_RECHECK)){
            //如果status是待复审，那么处理人就是总经理
            String post = Contant.POST_GM; //总经理
            Employee e = employeeDao.selectNameByPost(post);
            claimVoucher.setDealer(e);
        }else if(status.equals(Contant.CLAIMVOUCHER_APPROVED)){
            //如果status是已审核，那么处理人就是财务经理
            String post = Contant.POST_CASHIER;//财务经理
            Employee e = employeeDao.selectNameByPost(post);
            claimVoucher.setDealer(e);
        }
        return claimVoucher;
    }

    //查询报销单详情
    public List<ClaimVoucherItem> findItems(Integer cid) {
        return claimVoucherItemDao.selectItems(cid);
    }


    //查询记录信息
    public List<DealRecord> findRecords(Integer cid) {
        return dealRecordDao.selectRecord(cid);
    }

    //更新报销单
//    public void edit(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
//        //修改claim_voucher表中的状态
//        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
//        claimVoucher.setCreateTime(new Date());
//        Employee employee = employeeDao.selectNameByPost(Contant.POST_STAFF);
//        String nextDealSn = employee.getSn();
//        claimVoucher.setNextDealSn(nextDealSn);
//        claimVoucherDao.update(claimVoucher);
//
//
//        //根据claim_voucher_id 删除 claim_voucher_item中的数据
//        Integer cid = claimVoucher.getId();
//        claimVoucherItemDao.deleteByClaimVoucherId(cid);
//
//        //根据 items 添加修改完后的数据
//        for(ClaimVoucherItem item:items){
//            item.setClaimVoucherId(claimVoucher.getId());
//            claimVoucherItemDao.insertOne(item);
//        }
//
//        //添加一条记录到deal_record
//        addDealRecord(claimVoucher.getId(),Contant.DEAL_UPDATE,claimVoucher.getCreateSn());
//    }

    public void edit(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        //修改claim_voucher表中的数据
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucherDao.update(claimVoucher);

        //接下来处理claim_voucher_item
        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectItems(claimVoucher.getId());
        Boolean flag = false;
        //1、删除claim_voucher_item表中的数据
        //将 旧的数据 跟 修改后的数据 比较
        for(ClaimVoucherItem old:olds){
            for(ClaimVoucherItem item:items){
                if(old.getId() == item.getId()){//如果相等，则说明是有这条数据，就不进行删除
                    flag = true;
                    break;
                }
                flag = false;
            }
            if(!flag){
                claimVoucherItemDao.delete(old.getId());
            }
        }

        //2、更新或者增加一条 claim_vouchet_item数据
        for(ClaimVoucherItem item:items){
            item.setClaimVoucherId(claimVoucher.getId());
            if(item.getId() != null){
                claimVoucherItemDao.update(item);
            }else{//新增加的数据是没有id的
                claimVoucherItemDao.insertOne(item);
            }
        }



        //添加一条记录到deal_record
        addDealRecord(claimVoucher.getId(),Contant.DEAL_UPDATE,claimVoucher.getCreateSn());
    }


    //提交
    public void submit(Integer id) {
        //改变status为已提交
        String status = Contant.CLAIMVOUCHER_SUBMIT;
        String post = Contant.POST_FM;
        //提交后，处理人的名字变为部门经理
        Employee employee = employeeDao.selectNameByPost(post);
        String sn = employee.getSn();
        claimVoucherDao.updateStatus(id,status,sn);

        //增加一条记录，添加到deal_record表中
        ClaimVoucher claimVoucher = claimVoucherDao.selectCreateSnById(id);
        String dealSn = claimVoucher.getCreateSn();
        String dealWay = Contant.DEAL_UPDATE; //修改
        addDealRecord(id,dealWay,dealSn);

    }

    //审核
    public void check(DealRecord dealRecord,String dealSn,String post) {
        //查出总金额
        // 如果>5000（给总经理审批），则状态为待复审；如果<5000（给财务经理审批），则状态为已审核
        Integer cid = dealRecord.getClaimVoucherId();
        ClaimVoucher claimVoucher = claimVoucherDao.selectclaimVoucher(cid);
        Double totalAmount = claimVoucher.getTotalAmount();

        //获取处理方式：通过、打回、拒绝、打款
        String dealWay = dealRecord.getDealWay();

        //1、处理方式：通过
        if(dealWay.equals(Contant.DEAL_PASS)){
            //在通过的基础上，确定status：待复审  or 已审核
            //(1) 总额>5000,状态：待复审
            if(totalAmount>=5000){
                //总经理和部门经理处理>5000的报销单
                //a.总经理处理完后--状态：已审核
                if(post.equals(Contant.POST_GM)){//总经理
                    String status = Contant.CLAIMVOUCHER_APPROVED; //已审核
                    Employee employee = employeeDao.selectNameByPost(Contant.POST_CASHIER);//财务经理
                    String nextDealSn = employee.getSn(); //下一个处理人的sn

                    //添加记录到deal_record表中，dealSn:处理人
                    addDealRecord(cid,dealWay,dealSn);

                    //更新claim_voucher表中的状态
                    claimVoucherDao.updateStatus(cid,status,nextDealSn);

                }
                //b.部门经理处理完后--状态：待复审
                else if(post.equals(Contant.POST_FM)){//部门经理
                    String status = Contant.CLAIMVOUCHER_RECHECK;//待复审
                    Employee employee = employeeDao.selectNameByPost(Contant.POST_GM);//总经理
                    String nextDealSn = employee.getSn(); //下一个处理人的sn

                    //添加记录到deal_record表中，dealSn:处理人
                    addDealRecord(cid,dealWay,dealSn);

                    //更新claim_voucher表中的状态
                    claimVoucherDao.updateStatus(cid,status,nextDealSn);
                }
            }
            //(2) 总额<5000, 状态：已审核
            else{
                String status = Contant.CLAIMVOUCHER_APPROVED;//已审核
                Employee employee = employeeDao.selectNameByPost(Contant.POST_CASHIER); //财务经理
                String nextDealSn = employee.getSn();

                addDealRecord(cid,dealWay,dealSn);
                claimVoucherDao.updateStatus(cid,status,nextDealSn);
            }
        }
        //2、处理方式：打回
        else if(dealWay.equals(Contant.DEAL_BACK)){
            String status = Contant.CLAIMVOUCHER_BACK; //已打回

            //已打回：则处理人是员工自己
            addDealRecord(cid,dealWay,dealSn);
            claimVoucherDao.updateStatus(cid,status,dealSn);

        }
        //3、处理方式：拒绝
        else if(dealWay.equals(Contant.DEAL_REJECT)){//拒绝
            String status = Contant.CLAIMVOUCHER_TERMINATED; //已拒绝

            //已拒绝：则处理人是员工自己
            addDealRecord(cid,dealWay,dealSn);
            claimVoucherDao.updateStatus(cid,status,dealSn);
        }
        //4、处理方式：打款
        else if(dealWay.equals(Contant.DEAL_PAID)){//打款
            String status = Contant.CLAIMVOUCHER_PAID;//已打款

            //已打款：则没有处理人
            addDealRecord(cid,dealWay,dealSn);
            String overSn = null;
            claimVoucherDao.updateStatus(cid,status,overSn);

        }

    }

    //添加记录到deal_record表中
    public void addDealRecord(Integer cid,String dealWay,String dealSn){
        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealTime(new Date());
        dealRecord.setDealSn(dealSn);
        dealRecord.setClaimVoucherId(cid);
        dealRecord.setDealWay(dealWay);
        dealRecordDao.insertOne(dealRecord);
    }


}
