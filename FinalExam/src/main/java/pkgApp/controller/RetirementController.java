package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashmap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decmial
		hmTextFieldRegEx.put(txtYearsToWork, "\\d*?");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "\\d*(\\.\\d*)?");

		// Check out these pages (how to validate controls):
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format
		hmTextFieldRegEx.put(txtYearsToWork, "[0-9]|[1-3][0-9]|4[0]");
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "[0-9](\\.[0-9]{1,2}){0,1}|10(\\.0{1,2}){0,1}");
		hmTextFieldRegEx.put(txtYearsRetired, "[0-9]||1[0-9]||20");
		hmTextFieldRegEx.put(txtAnnualReturnRetired, "[0-9](\\\\.[0-9]{1,2}){0,1}|10(\\\\.0{1,2}){0,1}");
		hmTextFieldRegEx.put(txtRequiredIncome, "[2][6][4][2-9]|[2][6][5-9][0-9]|[2][7-9][0-9][0-9]|[3-9][0-9][0-9][0-9]|[1][0][0][0][0]");
		hmTextFieldRegEx.put(txtMonthlySSI, "[0-9]|[0-9][0-9]|[0-9][0-9][0-9]|[1][0-9][0-9][0-9]|[2][0-5][0-9][0-9]|[2][6][0-3][0-9]|[2][6][4][0-2]");
		
		
		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}
	}



	@FXML
	public void btnClear(ActionEvent event) {
		System.out.println("Clear pressed");

		// disable read-only controls
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.setDisable(true);

		// Clear, enable txtYearsToWork
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);

		for(TextField text: hmTextFieldRegEx.keySet()) {
			text.clear();
			text.setDisable(false);
		}
		// HashMap of all the input controls....!!!!
	}

	@FXML
	public void btnCalculate() {

		System.out.println("calculating");

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);
		int YearsToWork1= Integer.valueOf(txtYearsToWork.getText());
		double AnnualReturn1= Double.valueOf(txtAnnualReturnWorking.getText());
		int YearsRetired1= Integer.valueOf(txtYearsRetired.getText());
		double AnnualReturnRetired1= Double.valueOf(txtAnnualReturnRetired.getText());
		int RequiredIncome1= Integer.valueOf(txtRequiredIncome.getText());
		int MonthlySSI1=Integer.valueOf(txtMonthlySSI.getText());
		
		Retirement r=new Retirement(YearsToWork1, AnnualReturn1, YearsRetired1, AnnualReturnRetired1,
				RequiredIncome1, MonthlySSI1); 
	
		txtSaveEachMonth.setText((String.valueOf(r.MonthlySavings())));
		txtWhatYouNeedToSave.setText( String.valueOf(r.TotalAmountToSave()));
	}
}
