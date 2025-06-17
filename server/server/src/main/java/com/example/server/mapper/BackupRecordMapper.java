package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.BackupRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BackupRecordMapper extends BaseMapper<BackupRecord> {
    // 自定义方法：调用存储过程创建备份
    @Select("CALL BackupDatabase(#{comment})")
    Integer createBackup(@Param("comment") String comment);

    // 自定义方法：调用存储过程恢复备份
    @Select("CALL RestoreDatabase(#{backupId})")
    String restoreBackup(@Param("backupId") Integer backupId);

    // 自定义方法：调用存储过程清理备份
    @Select("CALL CleanBackupData(#{keepCount}, #{keepDays})")
    String cleanBackupData(@Param("keepCount") Integer keepCount, @Param("keepDays") Integer keepDays);

    // 自定义方法：调用存储过程列出所有备份记录
    @Select("CALL ListBackups()")
    List<BackupRecord> listBackups();
}

