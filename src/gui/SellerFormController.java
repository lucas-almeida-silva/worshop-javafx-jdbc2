package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//dependences
	private Seller entity;
	
	private SellerService service;
	
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
	
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close(); //fecha a janela após salvar
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public Seller getFormData() {
		Seller obj = new Seller();
		
		try {
			obj.setId(Utils.tryParseToInt(txtId.getText()));
			obj.setName(txtName.getText());
			obj.setEmail(txtEmail.getText());
			obj.setBirthDate(sdf.parse(txtBirthDate.getText()));
			obj.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));
			obj.setDepartment(new Department(Utils.tryParseToInt(txtDepartmentId.getText()), null));

		}
		catch(ParseException e ) {
			throw new ValidationException("Could not red the birthdate field");
		}
		
		return obj;
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
