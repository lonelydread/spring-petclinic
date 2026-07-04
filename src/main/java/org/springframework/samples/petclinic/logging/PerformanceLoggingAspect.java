package org.springframework.samples.petclinic.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceLoggingAspect {

	// Срез перехватывает все методы всех контроллеров в проекте
	@Around("execution(* org.springframework.samples.petclinic..*Controller.*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		String methodName = joinPoint.getSignature().toShortString();

		log.info("[AOP] Начат вызов метода: {}", methodName);

		try {
			Object proceed = joinPoint.proceed(); // Выполнение самого метода
			long executionTime = System.currentTimeMillis() - start;
			log.info("[AOP] Метод {} успешно выполнен за {} мс", methodName, executionTime);
			return proceed;
		} catch (Throwable e) {
			log.error("[AOP ОШИБКА] В методе {} произошел сбой: {}", methodName, e.getMessage());
			throw e;
		}
	}
}
