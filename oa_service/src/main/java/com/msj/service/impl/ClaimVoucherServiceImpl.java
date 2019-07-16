package com.msj.service.impl;

import com.msj.dao.ClaimVoucherDao;
import com.msj.dao.ClaimVoucherItemDao;
import com.msj.dao.DealRecordDao;
import com.msj.entity.ClaimVoucher;
import com.msj.entity.ClaimVoucherItem;
import com.msj.entity.DealRecord;
import com.msj.global.Contant;
import com.msj.service.ClaimVoucherService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClaimVoucherServiceImpl implements ClaimVoucherService{
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Autowired
    private DealRecordDao dealRecordDao;

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

    //查询个人报销单
    public List<ClaimVoucher> findSelf(String createSn) {
        return claimVoucherDao.selectSelf(createSn);
    }

    //查询待处理的报销单
    public List<ClaimVoucher> findForDeal(String post) {
        if(post.equals("总经理")){
            String status = Contant.CLAIMVOUCHER_RECHECK;
            return claimVoucherDao.selectForDeal(status);
        }else if(post.equals("部门经理")){
            String status = Contant.CLAIMVOUCHER_SUBMIT;
            return claimVoucherDao.selectForDeal(status);
        }else if(post.equals("财务经理")){
            String status = Contant.CLAIMVOUCHER_APPROVED;
            return claimVoucherDao.selectForDeal(status);
        }else{
            String status = Contant.CLAIMVOUCHER_CREATED;
            return claimVoucherDao.selectForDeal(status);
        }

    }

    //查询报销单
    public ClaimVoucher findclaimVoucher(Integer id) {
        return claimVoucherDao.selectclaimVoucher(id);
    }

    //查询报销单详情
    public List<ClaimVoucherItem> findItems(Integer cid) {
        return claimVoucherItemDao.selectItems(cid);
    }

    public List<DealRecord> findRecords(Integer id) {
        return null;
    }

    //查询记录信息
//    public List<DealRecord> findRecords(Integer id) {
//        return dealRecordDao.selectRecord(id);
//    }

    //更新报销单
    public void edit(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
        //更新报销单表
        claimVoucherDao.update(claimVoucher);

        //查询报销详情表
        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectItems(claimVoucher.getId());
        for(ClaimVoucherItem old:olds){
            boolean isHave = false;
            for(ClaimVoucherItem item:items){
                if(item.getId() == old.getId()){
                    isHave = true;
                    break;
                }
            }
            if(!isHave){
                //根据claim_voucher_item的主键进行删除
                claimVoucherItemDao.delete(old.getId());
            }
            for(ClaimVoucherItem item:items){
                item.setClaimVoucherId(claimVoucher.getId());
                if(item.getId()>0){
                    claimVoucherItemDao.update(item);
                }else{
                    claimVoucherItemDao.insertOne(item);
                }
            }
        }
    }

    public List<ClaimVoucher> selectClaimVoucherByPrice(Integer low, Integer high) {
        return claimVoucherDao.selectClaimVoucherByPrice(low,high);
    }

    public void submit(Integer id) {
//        claimVoucherDao
    }


}
