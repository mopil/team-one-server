package com.mjuteam2.TeamOne.member.controller;

import com.mjuteam2.TeamOne.member.config.SessionConst;
import com.mjuteam2.TeamOne.member.domain.Member;
import com.mjuteam2.TeamOne.member.dto.FindMemberForm;
import com.mjuteam2.TeamOne.member.dto.MemberListResponse;
import com.mjuteam2.TeamOne.member.dto.ResetPasswordForm;
import com.mjuteam2.TeamOne.member.dto.SignInForm;
import com.mjuteam2.TeamOne.member.service.SignInService;
import com.mjuteam2.TeamOne.util.dto.BoolResponse;
import com.mjuteam2.TeamOne.util.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.mjuteam2.TeamOne.util.dto.RestResponse.badRequest;
import static com.mjuteam2.TeamOne.util.dto.RestResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class SignInController {

    private final SignInService signInService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm loginForm,
                                   BindingResult bindingResult,
                                   HttpServletRequest request) throws LoginException {
        if (bindingResult.hasErrors()) {
            log.error("SignIn Errors = {}", bindingResult.getFieldErrors());
            return badRequest(ErrorResponse.convertJson(bindingResult.getFieldErrors()));
        }
        MemberListResponse memberListResponse = signInService.login(loginForm, request);
        log.info("member login = {}", memberListResponse);
        return success(memberListResponse);
    }

    /**
     * 로그아웃
     * 세션 삭제해서 로그아웃 진행
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        log.info("member logout = {}", request.getAttribute(SessionConst.LOGIN_MEMBER));
        signInService.logout(request);
        return success(new BoolResponse(true));
    }

    /**
     * 사용자 아이디 찾기
     */
    @PostMapping("/id")
    public ResponseEntity<?> findUserId(@Valid @RequestBody FindMemberForm form) {
        Member findMember = signInService.findByUserId(form);
        return success(findMember);
    }

    /**
     * 비밀번호 재설정
     */
    @PostMapping("/password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordForm form) {
        signInService.resetPassword(form);
        return success(new BoolResponse(true));
    }


}
