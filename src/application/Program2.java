package application;

import model.entities.Department;
import model.services.DepartmentService;

public class Program2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Department dp = new Department();
		
		DepartmentService dpService = new DepartmentService();
		dp = dpService.findById(2);
		System.out.println(dp);
	}

}
