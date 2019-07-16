package com.msj.dao;

import com.msj.entity.DealRecord;

import java.util.List;

public interface DealRecordDao {
    List<DealRecord> selectRecord(Integer id);
}
