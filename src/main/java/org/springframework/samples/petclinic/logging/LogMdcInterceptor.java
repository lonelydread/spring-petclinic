package org.springframework.samples.petclinic.logging;

import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

// Реализация контекстного логирования (MDC — Mapped Diagnostic Context)

/**
 * Используется для сквозного отслеживания запросов (особенно полезно при высокой
 * нагрузке). Позволяет привязать к текущему потоку выполнения уникальный ID запроса
 * (requestId) или имя пользователя, чтобы этот идентификатор автоматически выводился в
 * каждой строчке лога, сделанной внутри этого потока.
 */

public class LogMdcInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// Извлекаем или генерируем уникальный ID для цепочки логов
		String requestId = request.getHeader("X-Request-ID");
		if (requestId == null) {
			requestId = UUID.randomUUID().toString().substring(0, 8);
		}
		MDC.put("requestId", requestId);
		MDC.put("userIp", request.getRemoteAddr());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		MDC.clear(); // ОЧЕНЬ ВАЖНО!!!: очищаем поток, чтобы избежать утечки данных в пуле
						// потоков
	}

}
