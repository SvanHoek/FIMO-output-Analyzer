package TFBS_finder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;

public class Controller {
    private boolean FIMO_file = false;
    private boolean GenBank_file = false;
    private String FIMO_text = "";
    private String GenBank_text = "";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML // fx:id="myTextArea"
    private TextArea myTextArea;

    @FXML // fx:id="fimo_label"
    private Label fimo_label;

    @FXML // fx:id="genbank_label"
    private Label genbank_label;

    @FXML
    void initialize() {}
    public void reset(MouseEvent mouseEvent) {
        FIMO_file = false;
        FIMO_text = "";
        GenBank_file = false;
        GenBank_text = "";
        fimo_label.setTextFill(Color.BLACK);
        genbank_label.setTextFill(Color.BLACK);
        myTextArea.setText("");
    }

    @FXML
    void draggingOver(DragEvent event) {
        Dragboard board = event.getDragboard();
        if (board.hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    void dropping_FIMO(DragEvent event) {
        dropping(event, "FIMO");
    }
    @FXML
    void dropping_GenBank(DragEvent event) {
        dropping(event, "GenBank");
    }
    @FXML
    private void dropping(DragEvent event, String file_type) {
        try {
            Dragboard board = event.getDragboard();
            List<File> phil = board.getFiles();
            FileInputStream fis;
            fis = new FileInputStream(phil.get(0));

            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fis.read()) != -1){
                builder.append((char)ch);
            }

            fis.close();
            String temp = builder.toString();
            if (file_type.equals("FIMO")){
                if (temp.startsWith("motif_id\tmotif_alt_id")){ //"# motif_id\tmotif_alt_id" has to be used for FIMO ver <5.0.0
                    this.FIMO_file = true;
                    this.FIMO_text = temp;
                    fimo_label.setTextFill(Color.LIMEGREEN);
                } else {
                    fimo_label.setTextFill(Color.RED);
                }
            } else {
                if (temp.startsWith("LOCUS       ")){
                    this.GenBank_file = true;
                    this.GenBank_text = temp;
                    genbank_label.setTextFill(Color.LIMEGREEN);
                } else {
                    genbank_label.setTextFill(Color.RED);
                }
            }
            if (this.FIMO_file && this.GenBank_file){
                Find_TFBS find = new Find_TFBS();
                myTextArea.setText(find.go(this.FIMO_text , this.GenBank_text));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


}
