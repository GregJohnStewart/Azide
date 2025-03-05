package mil.army.dcgs.azide.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

@Singleton
public class OMapperCustomizer implements ObjectMapperCustomizer {
	
	@Override
	public void customize(ObjectMapper objectMapper) {
		objectMapper.registerModule(new JavaTimeModule());
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
}
