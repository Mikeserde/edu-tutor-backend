package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.OccupationTypeMapper;
import com.example.server.model.OccupationType;
import com.example.server.service.OccupationTypeService;
import org.springframework.stereotype.Service;

@Service
public class OccupationTypeServiceImpl extends ServiceImpl<OccupationTypeMapper, OccupationType>
        implements OccupationTypeService {
}
