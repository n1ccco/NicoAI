package org.bohdanzhuvak.nicoai.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "#root == null || #root instanceof T(org.bohdanzhuvak.nicoai.security.CustomUserDetails) ? #root.user : null")
public @interface CurrentUser {
}