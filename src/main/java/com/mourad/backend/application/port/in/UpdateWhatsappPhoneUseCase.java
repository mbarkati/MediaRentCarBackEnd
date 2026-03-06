package com.mourad.backend.application.port.in;

import com.mourad.backend.application.command.UpdateWhatsappPhoneCommand;
import com.mourad.backend.application.dto.WhatsappPhoneDto;

public interface UpdateWhatsappPhoneUseCase {
    WhatsappPhoneDto execute(UpdateWhatsappPhoneCommand command);
}
