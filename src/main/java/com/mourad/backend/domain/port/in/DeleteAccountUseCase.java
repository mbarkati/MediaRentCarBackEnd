package com.mourad.backend.domain.port.in;

public interface DeleteAccountUseCase {

    /** Delete the account that owns the given phone (called after JWT auth). */
    void deleteByPhone(String phone);

    /** Verify credentials then delete — used by the web form (no JWT). */
    void verifyAndDelete(String phone, String rawPassword);
}
