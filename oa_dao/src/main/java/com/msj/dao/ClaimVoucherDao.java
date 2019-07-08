package com.msj.dao;

import com.msj.entity.ClaimVoucher;
import com.msj.entity.ClaimVoucherItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClaimVoucherDao {
    void insertOne(ClaimVoucher claimVoucher);
    List<ClaimVoucher> selectSelf(String createSn);
    List<ClaimVoucher> selectForDeal(String createSn);
    ClaimVoucher selectclaimVoucher(Integer id);

    void update(ClaimVoucher claimVoucher);

    List<ClaimVoucher> selectClaimVoucherByPrice(@Param("low")Integer low,@Param("high")Integer high);
}
