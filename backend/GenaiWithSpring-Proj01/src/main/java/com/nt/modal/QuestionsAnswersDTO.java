package com.nt.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsAnswersDTO 
{
	private Long questionId;
    private String questionText;
    private String answer;

}
