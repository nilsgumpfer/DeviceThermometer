package ThermometerServer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Optional;


public class Controller {



    @FXML
    private Label lbl_Serverip;
    @FXML
    private Label lbl_Servername;
    @FXML
    private Label lbl_Serverport;
    @FXML
    private Label lbl_Serverstatus;
    @FXML
    private Label lbl_srvmsg;
    @FXML
    private Button btn_starteServer;
    @FXML
    private Button btn_stoppeServer;
    @FXML
    private TextArea ta_srvlog;
    /*@FXML
    private Label lbl_heizungtemp;*/
    @FXML
    private Button btn_setTemp;

    public static PrintStream ps;

    public Thermometer thermo1 = null;



    public void BTNServerStarten(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Name der Heizung definieren");
        dialog.setHeaderText("Heizung anlegen");
        dialog.setContentText("Bitte dieser Heizung einen Namen geben:");

        if(!(lbl_Servername.getText().equals("-"))){

        }
        else{
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent() == true && !result.get().equals("")){
            lbl_Servername.setText(result.get());}
            else{
                return;
            }
        }

        if(thermo1 == null){
            thermo1 = new Thermometer();
        }
        else{
            thermo1 = new Thermometer();
        }

        /*ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int i) throws IOException {
                ta_srvlog.appendText(String.valueOf((char) i));
            }
        });
        System.setOut(ps);*/

        /*Server wird gestartet*/

        lbl_srvmsg.setText(thermo1.startServer(lbl_Servername.getText()));
        lbl_Serverip.setText(thermo1.getServerIP());
        lbl_Servername.setText(thermo1.thermometername);
        lbl_Serverstatus.setText(thermo1.serverstatus);

        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(thermo1.serverport);
        String srvport = sb.toString();

        lbl_Serverport.setText(srvport);

        if(lbl_Serverstatus.getText() == "Gestartet"){
            btn_starteServer.setDisable(true);
            btn_stoppeServer.setDisable(false);
        }
    }

    public void BTNServerStoppen(ActionEvent event){
        lbl_srvmsg.setText(thermo1.stopServer());
        lbl_Serverstatus.setText(thermo1.serverstatus);

        if (lbl_Serverstatus.getText() == "Gestoppt"){
            btn_stoppeServer.setDisable(true);
            btn_starteServer.setDisable(false);
        }
    }

    public void BTNSetTemp(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Temperatur einstellen");
        dialog.setHeaderText("Temperatur des Thermometers einstellen");
        dialog.setContentText("Bitte Temperatur der Heizung einstellen:");
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent() == true && !result.get().equals("")) {
            Double newTemp = Double.parseDouble(result.get());
            //lbl_heizungtemp.setText(result.get() + "°C");
            System.out.println(newTemp);
            thermo1.setTemperatureSrv(newTemp);
        }
        else{
            return;
        }
    }

    public void BTNGetTemp(ActionEvent event)throws RemoteException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Aktuelle Temperatur des Thermometers");
        String aktuelleTemp = String.valueOf(thermo1.getTemperatureSrv());

        alert.setContentText(aktuelleTemp);

        alert.showAndWait();
    }
}
