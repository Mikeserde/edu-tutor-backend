package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.OccupationRegistrationMapper;
import com.example.server.model.OccupationRegistration;
import com.example.server.service.OccupationRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccupationRegistrationServiceImpl
        extends ServiceImpl<OccupationRegistrationMapper, OccupationRegistration>
        implements OccupationRegistrationService {

    @Autowired
    private OccupationRegistrationMapper registrationMapper;

    @Override
    public boolean isRegistrationUnique(Integer occupationTypeId, Integer studentId, Integer excludeId) {
        return registrationMapper.countByTypeAndStudent(occupationTypeId, studentId, excludeId) == 0;
    }
}