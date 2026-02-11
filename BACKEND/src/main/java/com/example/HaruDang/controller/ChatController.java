import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.example.HaruDang.dto.ChatRequest;
import com.example.HaruDang.dto.ChatResponse;
import com.example.HaruDang.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId;

        if (authentication instanceof AnonymousAuthenticationToken) {
            currentUserId = "anonymous"; // Or generate a unique ID for anonymous sessions if needed
        } else {
            // Logged-in user: get ID from cookie instead of authentication.getName()
            String userIdFromCookie = null;
            if (httpRequest.getCookies() != null) {
                for (Cookie cookie : httpRequest.getCookies()) {
                    if ("USER_ID".equals(cookie.getName())) { // Assuming the cookie name is "USER_ID"
                        userIdFromCookie = cookie.getValue();
                        break;
                    }
                }
            }

            if (userIdFromCookie != null) {
                currentUserId = userIdFromCookie;
            } else {
                // Fallback to authentication.getName() if cookie not found
                currentUserId = authentication.getName();
                // Optionally log a warning if USER_ID cookie is expected but not found
            }
        }

        String aiAnswer = chatService.getAnswer(request.getMessage());

        return ChatResponse.of(currentUserId, aiAnswer);
    }
}
