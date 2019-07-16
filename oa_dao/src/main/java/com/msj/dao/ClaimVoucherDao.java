package com.msj.dao;

import com.msj.entity.ClaimVoucher;
import com.msj.entity.ClaimVoucherItem;
import com.msj.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClaimVoucherDao {
    void insertOne(ClaimVoucher claimVoucher);
    List<ClaimVoucher> selectSelf(String createSn);
    List<ClaimVoucher> selectForDeal(String status);
    ClaimVoucher selectclaimVoucher(Integer id);
    void update(ClaimVoucher claimVoucher);


    List<ClaimVoucher> selectClaimVoucherStatus(String createSn);
}
