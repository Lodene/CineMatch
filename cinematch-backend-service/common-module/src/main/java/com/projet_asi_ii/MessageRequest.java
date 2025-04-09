package com.projet_asi_ii;

import java.io.Serializable;
import java.util.Map;

public class MessageRequest implements Serializable
{
	private String requestId;
	private String serviceId;
	private Map<String, Object> payload;

	public MessageRequest() {}

	public MessageRequest(String requestId, String serviceId, Map<String, Object> payload) {
		this.requestId = requestId;
		this.serviceId = serviceId;
		this.payload = payload;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getServiceId()
	{
		return serviceId;
	}

	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}

	public Map<String, Object> getPayload()
	{
		return payload;
	}

	public void setPayload(Map<String, Object> payload)
	{
		this.payload = payload;
	}
}