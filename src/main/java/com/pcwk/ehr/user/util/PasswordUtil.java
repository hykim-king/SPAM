package com.pcwk.ehr.user.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 비밀번호 해시 처리를 담당하는 유틸 클래스
 *
 * - 회원가입 시 사용자가 입력한 비밀번호를 해시 문자열로 바꿔 저장
 * - 로그인 시 사용자가 입력한 비밀번호도 같은 방식으로 해시 처리한 뒤 DB 값과 비교
 *
 * 현재 구현:
 * - SHA-256 사용
 * - 결과값은 64자리 16진수 문자열
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * 평문 비밀번호를 SHA-256 해시 문자열로 변환
     *
     * @param plainPassword 사용자가 입력한 평문 비밀번호
     * @return DB에 저장할 64자리 16진수 해시 문자열
     */
    public static String hash(String plainPassword) {
    	// null 값일 시 예외 발생
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        try {
        	// Java에서 해시 알고리즘을 사용할 때 쓰는 클래스 (SHA-256 알고리즘을 지정)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 문자열을 UTF-8 바이트 배열로 바꾼 뒤 SHA-256 해시 계산
            byte[] encodedHash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

            // 16진수 문자열로 변환
            return bytesToHex(encodedHash);

        } catch (NoSuchAlgorithmException e) {
        	// checked exception 처리
            throw new IllegalStateException("비밀번호 해시 처리 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 로그인 시 입력 비밀번호와 DB 저장 비밀번호가 같은지 비교
     *
     * @param plainPassword 사용자가 로그인 화면에 입력한 평문 비밀번호
     * @param savedHashPassword DB에 저장된 해시 비밀번호
     * @return 일치하면 true, 아니면 false
     */
    public static boolean matches(String plainPassword, String savedHashPassword) {
        if (plainPassword == null || savedHashPassword == null) {
            return false;
        }
        
        return hash(plainPassword).equals(savedHashPassword);
    }

    /**
     * SHA-256 결과 바이트 배열을 16진수 문자열로 변경
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();

        // 해시 결과 바이트를 하나씩 16진수로 변환
        for (byte b : hash) {
            //Java byte의 부호 문제를 피하고 0~255 값으로 보기
            String hex = Integer.toHexString(0xff & b);

            // 16진수 한 자리 값이면 앞에 0을 붙여 두 자리로 맞춤
            if (hex.length() == 1) {
                hexString.append('0');
            }

            // 변환한 16진수 문자열을 누적
            hexString.append(hex);
        }

        // 최종 16진수 문자열을 반환
        return hexString.toString();
    }
}
