package com.mourad.backend.infrastructure.config;

import com.mourad.backend.application.service.GetWhatsappPhoneService;
import com.mourad.backend.domain.model.AppConfig;
import com.mourad.backend.domain.port.out.AppConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(2)
public class AppConfigSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AppConfigSeeder.class);
    private static final String DEFAULT_WHATSAPP_PHONE = "212600851481";

    private final AppConfigRepository configRepository;

    public AppConfigSeeder(AppConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (configRepository.findByKey(GetWhatsappPhoneService.KEY).isPresent()) {
            log.debug("WhatsApp phone config already exists — skipping seed.");
            return;
        }
        configRepository.save(AppConfig.create(GetWhatsappPhoneService.KEY, DEFAULT_WHATSAPP_PHONE));
        log.info("Default WhatsApp phone seeded: {}", DEFAULT_WHATSAPP_PHONE);
    }
}
