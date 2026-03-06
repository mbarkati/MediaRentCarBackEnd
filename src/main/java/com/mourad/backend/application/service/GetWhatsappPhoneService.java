package com.mourad.backend.application.service;

import com.mourad.backend.application.dto.WhatsappPhoneDto;
import com.mourad.backend.application.port.in.GetWhatsappPhoneUseCase;
import com.mourad.backend.domain.port.out.AppConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetWhatsappPhoneService implements GetWhatsappPhoneUseCase {

    public static final String KEY = "whatsapp_phone";

    private final AppConfigRepository configRepository;

    public GetWhatsappPhoneService(AppConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public WhatsappPhoneDto execute() {
        return configRepository.findByKey(KEY)
                .map(c -> new WhatsappPhoneDto(c.getValue()))
                .orElse(new WhatsappPhoneDto(""));
    }
}
