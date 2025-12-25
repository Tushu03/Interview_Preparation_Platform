package com.nt.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewQuestion 
{
	private int id;
    private String questiontext;
    private String category;
    private String difficulty;

}
