package com.example.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.model.OccupationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OccupationTypeMapper extends BaseMapper<OccupationType> {

}