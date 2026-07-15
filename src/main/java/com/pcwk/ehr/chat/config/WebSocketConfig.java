package com.pcwk.ehr.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.pcwk.ehr.chat.handler.ChatHandler;
import com.pcwk.ehr.chat.handler.ChatHandshakeInterceptor;

@Configuration		// 이 클래스가 스프링 설정임을 표시
@EnableWebSocket	// 웹소켓 기능을 켠다
// 소켓 연결 설정 클래스
public class WebSocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		registry.addHandler(chatHandler(), "/ws/chat.do") // "/ws/chat" 주소로 오면 chatHandler가 처리
				.addInterceptors(new ChatHandshakeInterceptor())
				.setAllowedOrigins("*");		   	   // 어느 출처에서든 연결 허용 (개발용)

	}
	
    @Bean
    public ChatHandler chatHandler() {
        return new ChatHandler();
    }	

}
