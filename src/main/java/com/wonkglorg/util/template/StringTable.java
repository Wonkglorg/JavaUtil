package com.wonkglorg.util.template;

import java.util.List;

public class StringTable {



	public void createTable(List<? extends Record> records){

		if(records.size() <= 0){
			return;
		}

		//todo:jmd get all fields with reflection and display them in a table.

		var record = records.get(0);
	//	Reflection


	}

}
