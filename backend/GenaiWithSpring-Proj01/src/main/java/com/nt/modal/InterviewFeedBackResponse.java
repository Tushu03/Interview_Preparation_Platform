package com.nt.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewFeedBackResponse 
{
	private Long questionId;
    private String feedback;
    private int score;

}
