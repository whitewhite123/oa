package com.msj.dao;

import com.msj.entity.DealRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DealRecordDao {
    List<DealRecord> selectRecord(Integer cid);

    void insertOne(DealRecord dealRecord);

    DealRecord selectIsRecord(@Param("cid")Integer cid, @Param("dealWay")String dealWay);
}
