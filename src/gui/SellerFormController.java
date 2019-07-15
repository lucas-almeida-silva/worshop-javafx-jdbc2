package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Seller;

public class SellerFormController implements Initializable {
	
	//dependences
	private Seller entity;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtBirthDate;
	
	@FXML
	private TextField txtBaseSalary;
	
	@FXML
	private TextField txtDepartmentId;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;	
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("obBtSaveAction");
		
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("obBtCancelAction");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();	
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtEmail, 30);
		Constraints.setTextFieldMaxLength(txtBirthDate, 10);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtBaseSalary, 10);
		Constraints.setTextFieldInteger(txtDepartmentId);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		
		if (entity.getBirthDate() != null) {
            txtBirthDate.setText(String.valueOf(entity.getBirthDate()));
        }
		
		txtBaseSalary.setText(String.valueOf(entity.getBaseSalary()));
	
		
		if (entity.getDepartment() != null) {
            txtDepartmentId.setText(String.valueOf(entity.getDepartment().getId()));
        }
	}

}
