package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.model.OccupationRegistration;

public interface OccupationRegistrationService extends IService<OccupationRegistration> {
    boolean isRegistrationUnique(Integer occupationTypeId, Integer studentId, Integer excludeId);
}