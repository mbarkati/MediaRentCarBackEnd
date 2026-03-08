package com.mourad.backend.interfaces.web;

import com.mourad.backend.domain.exception.InvalidCredentialsException;
import com.mourad.backend.domain.port.in.DeleteAccountUseCase;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class DeleteAccountWebController {

    private final DeleteAccountUseCase deleteAccountUseCase;

    public DeleteAccountWebController(DeleteAccountUseCase deleteAccountUseCase) {
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    @GetMapping(value = "/delete-account", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String showForm(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String success) {

        String banner = "";
        if ("true".equals(success)) {
            banner = """
                    <div class="banner success">
                      Your account has been permanently deleted. We are sorry to see you go.
                    </div>
                    """;
        } else if ("credentials".equals(error)) {
            banner = """
                    <div class="banner error">
                      Incorrect phone number or password. Please try again.
                    </div>
                    """;
        }

        boolean showForm = !"true".equals(success);

        String form = showForm ? """
                <form method="POST" action="/delete-account">
                  <label for="phone">Phone number</label>
                  <input id="phone" name="phone" type="tel"
                         placeholder="+212600123456" required autocomplete="tel">

                  <label for="password">Password</label>
                  <input id="password" name="password" type="password"
                         placeholder="Your password" required autocomplete="current-password">

                  <button type="submit">Delete my account</button>
                </form>
                """ : "";

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Delete Account — MediaRentCar</title>
                  <style>
                    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
                    body {
                      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                      background: #f7f8fa;
                      display: flex;
                      justify-content: center;
                      padding: 48px 16px;
                      min-height: 100vh;
                    }
                    .card {
                      background: #fff;
                      border-radius: 12px;
                      box-shadow: 0 2px 16px rgba(0,0,0,0.08);
                      padding: 40px 36px;
                      width: 100%%;
                      max-width: 440px;
                      align-self: flex-start;
                    }
                    .logo { font-size: 1.1rem; font-weight: 700; color: #e63946; margin-bottom: 24px; }
                    h1 { font-size: 1.35rem; color: #111; margin-bottom: 8px; }
                    .subtitle {
                      font-size: 0.875rem; color: #666; margin-bottom: 24px; line-height: 1.5;
                    }
                    .warning {
                      background: #fff8e1; border: 1px solid #ffe082; border-radius: 8px;
                      padding: 12px 14px; font-size: 0.82rem; color: #795548;
                      margin-bottom: 24px; line-height: 1.5;
                    }
                    .banner {
                      border-radius: 8px; padding: 14px 16px; margin-bottom: 20px;
                      font-size: 0.9rem; line-height: 1.5;
                    }
                    .banner.error   { background: #fef2f2; border: 1px solid #fca5a5; color: #991b1b; }
                    .banner.success { background: #f0fdf4; border: 1px solid #86efac; color: #166534; }
                    label {
                      display: block; font-size: 0.85rem; font-weight: 600;
                      color: #333; margin-bottom: 6px; margin-top: 18px;
                    }
                    input {
                      width: 100%%; padding: 11px 13px; border: 1px solid #d1d5db;
                      border-radius: 8px; font-size: 0.95rem; color: #111;
                      outline: none; transition: border-color 0.15s;
                    }
                    input:focus { border-color: #e63946; }
                    button {
                      margin-top: 28px; width: 100%%; padding: 13px;
                      background: #e63946; color: #fff; border: none;
                      border-radius: 8px; font-size: 1rem; font-weight: 600;
                      cursor: pointer; transition: background 0.15s;
                    }
                    button:hover { background: #c1121f; }
                    .footer {
                      margin-top: 28px; font-size: 0.78rem; color: #999; text-align: center;
                    }
                  </style>
                </head>
                <body>
                  <div class="card">
                    <div class="logo">MediaRentCar</div>
                    <h1>Delete your account</h1>
                    <p class="subtitle">
                      Enter your phone number and password to permanently delete your account.
                    </p>
                    <div class="warning">
                      ⚠️ This action is <strong>irreversible</strong>. Your profile and all associated
                      data will be permanently removed from our systems.
                    </div>
                %s
                %s
                    <p class="footer">MediaRentCar &mdash; Account Management</p>
                  </div>
                </body>
                </html>
                """.formatted(banner, form);
    }

    @PostMapping(value = "/delete-account", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void handleDelete(
            @RequestParam String phone,
            @RequestParam String password,
            HttpServletResponse response) throws IOException {
        try {
            deleteAccountUseCase.verifyAndDelete(phone.strip(), password);
            redirect(response, "/delete-account?success=true");
        } catch (InvalidCredentialsException e) {
            redirect(response, "/delete-account?error=credentials");
        }
    }

    /** 303 See Other — browsers and curl always switch to GET after this. */
    private static void redirect(HttpServletResponse response, String location) throws IOException {
        response.setStatus(HttpServletResponse.SC_SEE_OTHER); // 303
        response.setHeader("Location", location);
    }
}
