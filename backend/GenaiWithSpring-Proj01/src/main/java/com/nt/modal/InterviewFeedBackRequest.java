package com.nt.modal;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewFeedBackRequest 
{
	
	private List<QuestionsAnswersDTO> responses;

}
