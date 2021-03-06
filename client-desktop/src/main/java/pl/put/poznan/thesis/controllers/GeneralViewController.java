package pl.put.poznan.thesis.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import pl.put.poznan.thesis.controllers.generalsubviews.GeneralSubviewController;
import pl.put.poznan.thesis.gui.utils.DialogFactory;
import pl.put.poznan.thesis.gui.utils.builder.BudgetDialogBuilder;
import pl.put.poznan.thesis.gui.utils.builder.WarningBuilder;
import pl.put.poznan.thesis.model.Budget;
import pl.put.poznan.thesis.service.GeneralViewService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class GeneralViewController implements Controller {
    private GeneralViewService generalViewService;

    @FXML
    private ComboBox<Budget> budgetPicker;
    @FXML
    private GeneralSubviewController budgetManagementController, budgetOverviewController;

    @Inject
    public GeneralViewController(GeneralViewService generalViewService) {
        this.generalViewService = generalViewService;
    }

    @Override
    public void clear() {
        budgetPicker.setItems(null);
        budgetManagementController.clear();
        budgetOverviewController.clear();
    }

    @Override
    public void refresh() {
        List<Budget> budgets = generalViewService.getBudgets();
        budgetPicker.setItems(FXCollections.observableArrayList(budgets));
        budgetManagementController.refresh();
        budgetOverviewController.refresh();
    }

    @FXML
    private void initialize() {
        budgetManagementController.setGeneralViewController(this);
        budgetOverviewController.setGeneralViewController(this);
    }

    @FXML
    private void createBudget() {
        boolean operationSuccessful = false;
        Optional<String> name = DialogFactory.get(new BudgetDialogBuilder()).showAndWait();
        if (name.isPresent()) {
            operationSuccessful = generalViewService.addBudget(name.get());
            if (operationSuccessful) {
                refresh();
            } else {
                DialogFactory.get(new WarningBuilder("Budget creation error",
                        "Budget named " + name.get() + " is already created and owned by you.")).showAndWait();
            }
        }
    }

    @FXML
    private void onBudgetSelection() {
        Budget selectedBudget = budgetPicker.getSelectionModel().getSelectedItem();
        budgetManagementController.setSelectedBudget(selectedBudget);
        budgetOverviewController.setSelectedBudget(selectedBudget);
        budgetOverviewController.refresh();
        budgetManagementController.refresh();
    }
}
