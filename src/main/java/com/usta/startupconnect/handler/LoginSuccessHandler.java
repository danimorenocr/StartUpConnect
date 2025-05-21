package com.usta.startupconnect.handler;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import java.io.IOException;
// import jakarta.servlet.ServletException;
// import org.springframework.web.servlet.FlashMap;
// import org.springframework.web.servlet.support.SessionFlashMapManager;



// @Component
// public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request,
//                                         HttpServletResponse response,
//                                         Authentication authentication) throws IOException, ServletException {
//         SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
//         FlashMap flashMap = new FlashMap();
//         flashMap.put("success", "Has iniciado sesión correctamente, " + authentication.getName());
//         flashMapManager.saveOutputFlashMap(flashMap, request, response);
//         super.onAuthenticationSuccess(request, response, authentication);
//     }

// }
