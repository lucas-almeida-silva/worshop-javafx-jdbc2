package gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DialogForm, DataChangeListener{
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//dependence
	private SellerService service;
	
	@FXML
	private TableView<Seller> tableViewSeller;
	
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> tableColumnName;

	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	@FXML
	private TableColumn<Seller, Department> tableColumnDepartmentId;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	
	@FXML 
	private Button btNew;
	
	@FXML
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event); //pega o palco atual
		Seller obj = new Seller();
		createDialogForm(obj,"/gui/SellerForm.fxml", parentStage);
	}
	
	//Injeção de dependência
	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty()); //a tableview preenche a tela	
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("BaseSalary"));
		initializeDepartmentIdColumn();
		initializeBirthDateColumn();
		
	}
	
	 public void updateTableView() {
	        if (service == null) {
	            throw new IllegalStateException("Service was null");
	        } else {
	            List<Seller> list = service.findAll();
	            obsList = FXCollections.observableArrayList(list);
	            tableViewSeller.setItems(obsList);
	            initEditButtons();
	        }
	    }
	 private void initializeDepartmentIdColumn() {
		 tableColumnDepartmentId.setCellValueFactory(new PropertyValueFactory<>("department"));
	     
		 tableColumnDepartmentId.setCellFactory(param -> new TableCell<>() {
	         @Override
	         protected void updateItem(Department department, boolean b) {
	             super.updateItem(department, b);
	             
	             if (department == null) {
	                 setText(null);
	             } else {
	                 setText(String.valueOf(department.getId()));
	             }
	         }
	     });
	 }
	 
	 private void initializeBirthDateColumn() {
		 tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
	        
	        tableColumnBirthDate.setCellFactory(param -> new TableCell<>() {
	           
	            
	            @Override
	            public void updateItem(Date date, boolean empty) {
	                if (date == null) {
	                    setText(null);
	                } else {
	                    setText(sdf.format(date));
	                }
	            }
	        });
	 }

	@Override
	public <Seller> void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		
		try {
			//carregamento da view
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			//instanciando o seller
			SellerFormController controller = loader.getController();
			controller.setSeller((model.entities.Seller) obj);
			controller.setSellerService(new SellerService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); //não permite redimensionar a tela
			dialogStage.initOwner(parentStage); //definição do pai da janela
			dialogStage.initModality(Modality.WINDOW_MODAL); //definie a janela como modal
			dialogStage.showAndWait(); //exibe a janela
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	 
	 
}



