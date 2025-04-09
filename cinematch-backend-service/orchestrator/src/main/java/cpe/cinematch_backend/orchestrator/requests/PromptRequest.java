package cpe.cinematch_backend.orchestrator.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromptRequest
{
	String name;

	String prompt;
}
