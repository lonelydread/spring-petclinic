package org.springframework.samples.petclinic.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.Setter;

import java.util.Map;

@Setter
public class MdcTargetFilter extends AbstractMatcherFilter<ILoggingEvent> {

	private String mdcKey;
	private String mdcValue;

	public void setMdcKey(String mdcKey) {
		this.mdcKey = mdcKey;
	}

	public void setMdcValue(String mdcValue) {
		this.mdcValue = mdcValue;
	}

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (this.mdcKey == null || this.mdcValue == null) {
			return FilterReply.NEUTRAL;
		}

		// Безопасное извлечение (защита от NullPointerException)
		Map<String, String> mdcMap = event.getMDCPropertyMap();
		String value = (mdcMap != null) ? mdcMap.get(this.mdcKey) : null;

		if (this.mdcValue.equals(value)) {
			return this.onMatch; // Вернет то, что написано в <onMatch>
		} else {
			return this.onMismatch; // Вернет то, что написано в <onMismatch>
		}
	}
}
