/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author Ayman
 */
public class ShowPop {

    private ContextMenu pop = new ContextMenu();

    public void showPop(ArrayList<String> list, JFXTextField textField) {
        if (textField.getText().equals("")) {
            pop.hide();
            return;
        }
        pop.getItems().clear();
        for (String string1 : list) {
            MenuItem m = new MenuItem();
            m.getStyleClass().add("menu");
            m.setStyle("-fx-pref-width:" + textField.getWidth() + ";");
            m.setOnAction((event) -> {
                pop.hide();
                textField.setText(m.getText());
            });
            pop.getItems().add(m);
        }

        textField.setContextMenu(pop);
        pop.show(textField, Side.BOTTOM, 0, 0);
    }

   
  
}
