package com.ruoyi.system.api.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CustomAuthorityDeserializer extends JsonDeserializer {
	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) p.getCodec();
		JsonNode jsonNode = mapper.readTree(p);//读取json数据
		List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
		Iterator<JsonNode> elements = jsonNode.elements();//
		while (elements.hasNext()){
			JsonNode next = elements.next();
			JsonNode authority = next.get("authority");
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
 
		}
		return grantedAuthorities;
	}
}