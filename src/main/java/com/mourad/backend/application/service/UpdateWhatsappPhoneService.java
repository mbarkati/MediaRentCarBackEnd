package com.mourad.backend.application.service;

import com.mourad.backend.application.command.UpdateWhatsappPhoneCommand;
import com.mourad.backend.application.dto.WhatsappPhoneDto;
import com.mourad.backend.application.port.in.UpdateWhatsappPhoneUseCase;
import com.mourad.backend.domain.exception.InvalidCarStateException;
import com.mourad.backend.domain.model.AppConfig;
import com.mourad.backend.domain.port.out.AppConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateWhatsappPhoneService implements UpdateWhatsappPhoneUseCase {

    private final AppConfigRepository configRepository;

    public UpdateWhatsappPhoneService(AppConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public WhatsappPhoneDto execute(UpdateWhatsappPhoneCommand command) {
        validatePhone(command.phone());

        AppConfig config = configRepository.findByKey(GetWhatsappPhoneService.KEY)
                .orElseGet(() -> AppConfig.create(GetWhatsappPhoneService.KEY, command.phone()));
        config.updateValue(command.phone());
        return new WhatsappPhoneDto(configRepository.save(config).getValue());
    }

    private static void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new InvalidCarStateException("Phone number must not be blank");
        }
        if (!phone.matches("\\d+")) {
            throw new InvalidCarStateException(
                    "Phone number must contain digits only (no +, spaces or dashes)");
        }
        if (phone.length() < 10 || phone.length() > 15) {
            throw new InvalidCarStateException(
                    "Phone number must be between 10 and 15 digits (got: " + phone.length() + ")");
        }
    }
}
