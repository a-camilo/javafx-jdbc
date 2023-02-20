package com.camilo.antonio.javafxjdbc;

import com.camilo.antonio.javafxjdbc.gui.listeners.DataChangeListener;
import com.camilo.antonio.javafxjdbc.gui.util.Alerts;
import com.camilo.antonio.javafxjdbc.gui.util.Constraints;
import com.camilo.antonio.javafxjdbc.gui.util.Utils;
import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.entities.Seller;
import model.exceptions.ValidateException;
import model.services.SellerService;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController implements Initializable {

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private Seller entity;

    private SellerService service;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dateBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setSellerService(SellerService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();

        } catch (ValidateException e) {
            setErrorMessages(e.getErrors());
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller obj = new Seller();

        ValidateException exception = new ValidateException("Validation error");

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            exception.addError("name", "Field can´t be empty");
        }
        obj.setName(txtName.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        return obj;
    }

    @FXML
    public void onBtnCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();

    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Utils.formatDatePicker(dateBirthDate, "dd/MM/yyyy");
    }

    public void updateFormData() {

        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
        if (entity.getBirthDate() != null) {
            dateBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }
    }

    private void setErrorMessages(Map<String, String> error) {

        Set<String> fields = error.keySet();

        if (fields.contains("name")) {
            labelErrorName.setText(error.get("name"));
        }
    }
}