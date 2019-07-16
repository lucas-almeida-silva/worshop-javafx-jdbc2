package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//dependences
	private Seller entity;
	
	private SellerService service;
	
	private DepartmentService departmentService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Label labelErrorDepartmentId;
	
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
	
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	
	@FXML
	public void onBtSaveAction(ActionEvent event) throws ParseException {
		
		clearFields();
		
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close(); //fecha a janela após salvar
		}
		catch(ValidationException e) {
			setErrorsMessage(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	public Seller getFormData() throws ParseException {
		Seller obj = new Seller();
		
		ValidationException exception = new ValidationException("Validation error");
		
			obj.setId(Utils.tryParseToInt(txtId.getText()));
			
			
			if(txtName.getText() == null || txtName.getText().trim().equals("")) {
				exception.addError("name", "Field can't be empty");
			}
			else {
				obj.setName(txtName.getText());
			}
			
			
			if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
				exception.addError("email", "Field can't be empty");
			}
			else {
				obj.setEmail(txtEmail.getText());
			}
			
			
			if(txtBaseSalary.getText() == null || txtBaseSalary.getText().equals("")) {
				exception.addError("baseSalary", "Field can't be empty");
			}
			else {
				obj.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));
			}
		
			if(txtDepartmentId.getText() == null || txtDepartmentId.getText().equals("")) {
				exception.addError("departmentId", "Field can't by empty");
			}
			
			else {
				Department department = new Department();
				department = departmentService.findById(Integer.parseInt(txtDepartmentId.getText()));
				if(department == null) {
					exception.addError("departmentId", "Department not exists");
				}
				else {
					obj.setDepartment(department);
				}
			}
			if(txtBirthDate.getText() == null || txtBirthDate.getText().trim().equals("")) {
				exception.addError("birthDate", "Field can't be empty");
			}
			else {
				try {
					obj.setBirthDate(sdf.parse(txtBirthDate.getText()));
				} 
				catch (ValidationException e) {
				   exception.addError("birthDate", "Field isn't a valid date");
				}
			}
			
			if(exception.getErrors().size() > 0) {
				throw exception;
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
		
		setDepartmentService(new DepartmentService());
	}
	
	private void setErrorsMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
			
		}
		if(fields.contains("email")) {
			labelErrorEmail.setText(errors.get("email"));
		}
		if(fields.contains("birthDate")) {
			labelErrorBirthDate.setText(errors.get("birthDate"));
		}
		if(fields.contains("birthDate2")) {
			labelErrorBirthDate.setText(errors.get("birthDate2"));
		}
		if(fields.contains("baseSalary")) {
			labelErrorBaseSalary.setText(errors.get("baseSalary"));
		}
		if(fields.contains("departmentId")) {
			labelErrorDepartmentId.setText(errors.get("departmentId"));
		}
	}
	
	private void clearFields() {
		labelErrorName.setText("");
		labelErrorEmail.setText("");
		labelErrorBirthDate.setText("");
		labelErrorBaseSalary.setText("");
		labelErrorDepartmentId.setText("");
	}

}
