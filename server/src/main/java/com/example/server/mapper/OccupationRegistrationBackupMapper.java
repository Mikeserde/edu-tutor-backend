package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.OccupationRegistrationBackup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OccupationRegistrationBackupMapper extends BaseMapper<OccupationRegistrationBackup> {
    // 由于备份操作通过存储过程完成，这里不需要额外的自定义方法
}