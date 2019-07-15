package gui;

import javafx.stage.Stage;

public interface DialogForm {
	
	<T> void createDialogForm(T obj, String absoluteName, Stage parentStage);

}
