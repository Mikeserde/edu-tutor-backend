package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.OccupationScheduleBackup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OccupationScheduleBackupMapper extends BaseMapper<OccupationScheduleBackup> {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}