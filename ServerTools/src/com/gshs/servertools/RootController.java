package com.gshs.servertools;


import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gshs.servertools.about.About;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.*;




public class RootController implements Initializable {
    
    private String username, host, password;
    Calendar now;
    long lastupdate = 0;
    long startTime = 0;
    private double xOffset = 0;
    private double yOffset = 0;
    private int port;
    private boolean paused = true;
    WebEngine docWebEngine, hostWebEngine, helpWebEngine;
    static About aboutPnl = new About();
    @FXML
    private Circle indicator;
    @FXML
    private AnchorPane archRoot;
    @FXML
    private Button sftpSend;
    @FXML
    private Button sftpGet;
    @FXML
    private StackPane pnlStack;
    @FXML
    private Pane monitorPane;
    @FXML
    private Pane loginPane;
    @FXML
    private Pane pnlSignUp;
    @FXML
    private ImageView btnBack;
    @FXML
    private Button btnConfigure;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnSSH;
    @FXML
    private Button btnWeb;
    @FXML
    private MenuButton searchFile;
    @FXML
    private MenuItem btnSearchFile;
    @FXML
    private MenuItem btnSearchDir;
    @FXML
    private Pane pnlSignIn;
    @FXML
    private TextField sshHost;
    @FXML
    private TextField sshPort;
    @FXML
    private TextField sshUsername;
    @FXML
    private TextField filePathPC;
    @FXML
    private TextField filePathServer;
    @FXML
    private PasswordField sshPassword;
    @FXML
    private MenuItem menuReconnect;
    @FXML
    private MenuItem menuStop;
    @FXML
    private MenuItem menuAbout;
    @FXML
    private MenuItem menuLogout;
    @FXML
    private MenuItem menuClear;
    @FXML
    private Label labelFailed;
    @FXML
    private WebView docWeb;
    @FXML
    private WebView hostView;
    @FXML
    private WebView viewHelp;
    @FXML
    private Label boardHost;
    @FXML
    private Label boardPort;
    @FXML
    private Label boardUsername;
    @FXML
    private Label boardDivision;
    @FXML
    private Label boardID;
    @FXML
    private Label boardStart;
    @FXML
    private TextField webField;
    @FXML
    private Hyperlink gshs;
    @FXML
    private Hyperlink bfs;
    @FXML
    private Hyperlink kakao;
    @FXML
    private Hyperlink over;
    @FXML
    private Hyperlink git;

    @FXML
    private LineChart<String, Number> cpuchart, ramchart, gpumemchart, gpuusechart;
    int ctr = 0;
    
    XYChart.Series<String, Number> series_cpu, series_mem;
    XYChart.Series<String, Number>[] series_gpumem = (XYChart.Series<String, Number>[]) new XYChart.Series[6];
    XYChart.Series<String, Number>[] series_gpuuse = (XYChart.Series<String, Number>[]) new XYChart.Series[6];
    
    SSHClient server;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource().equals(gshs)) {
            Desktop d = Desktop.getDesktop();
            try
            {
                d.browse(new URI("http://115.23.235.135"));
            }
            catch(Exception e)
            {
                
            }
        }
        if(event.getSource().equals(over)) {
            Desktop d = Desktop.getDesktop();
            try
            {
                d.browse(new URI("https://www.overleaf.com/project/60a1d2365a26a733e5a9b16f"));
            }
            catch(Exception e)
            {
                
            }
        }
        if(event.getSource().equals(kakao)) {
            Desktop d = Desktop.getDesktop();
            try
            {
                d.browse(new URI("https://open.kakao.com/o/gL8MCked"));
            }
            catch(Exception e)
            {
                
            }
        }
        if(event.getSource().equals(bfs)) {
            Desktop d = Desktop.getDesktop();
            try
            {
                d.browse(new URI("https://moodle.bfstudy.net/course/view.php?id=83"));
            }
            catch(Exception e)
            {
                
            }
        }
        if(event.getSource().equals(git)) {
            Desktop d = Desktop.getDesktop();
            try
            {
                d.browse(new URI("https://github.com/gshslinuxintro"));
            }
            catch(Exception e)
            {
                
            }
        }
        if(event.getSource().equals(btnConfigure)) {
            pnlSignUp.toFront();
        }
        if(event.getSource().equals(btnSave)) {
            pnlSignUp.toFront();
            host = sshHost.getText();
            sshHost.setText(host);
            port = Integer.parseInt(sshPort.getText());
            sshPort.setText(port+"");
        }
        if(event.getSource().equals(btnWeb) || event.getSource().equals(webField)) {
            System.out.println("btnWeb");
            WebEngine webEngine = hostView.getEngine();

            webEngine.load(webField.getText());
        }
        if(event.getSource().equals(btnSSH) || event.getSource().equals(sshUsername) || event.getSource().equals(sshPassword)) {

            host = sshHost.getText();
            sshHost.setText(host);
            port = Integer.parseInt(sshPort.getText());
            sshPort.setText(port+"");
            
            username = sshUsername.getText();
            password = sshPassword.getText();
            if(connect(host, port, username, password))
            {
                System.out.println("User: "+username+"  Host: "+host);
                System.out.println("SSH Connection Established. Retriving data.");
                monitorPane.toFront();
            }
            else
            {
                
            }
        }
        if(event.getSource().equals(btnSearchFile)) {

            FileChooser chooser = new FileChooser();
            File selectedFile = chooser.showOpenDialog(searchFile.getScene().getWindow());
            filePathPC.setText(""+selectedFile.getAbsoluteFile());
        }

        if(event.getSource().equals(btnSearchDir)) {
            DirectoryChooser chooser = new DirectoryChooser();
            File selectedDirectory  = chooser.showDialog(searchFile.getScene().getWindow());
            filePathPC.setText(""+selectedDirectory.getAbsolutePath());
        }
        if(event.getSource().equals(sftpGet)) {
            System.out.println("sftpGet");
            try
            {
                System.out.println("download");
                server.download(filePathServer.getText(), filePathPC.getText());
                System.out.println("download fin");
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
            }
        }
        if(event.getSource().equals(sftpSend)) {
            System.out.println("sftpSend");
            try
            {
                System.out.println("upload");
                server.upload(filePathPC.getText(), filePathServer.getText());
                System.out.println("upload fin");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if(event.getSource().equals(btnBack)) {
            //new ZoomIn(pnlSignIn).play();
            pnlSignIn.toFront();
        }
    }
    
    @FXML
    private void handleMenuEvent(ActionEvent event) {
        if(event.getSource() == menuLogout) {
            System.out.println("menuLogout");
            Alert dialog = new Alert(AlertType.CONFIRMATION);
            dialog.initStyle(StageStyle.TRANSPARENT);
            //dialog.setTitle("Server Monitor Alert");
            dialog.setHeaderText("Sign out");
            dialog.setContentText("   Do you realy want to exit?");
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom left, #42ABCB, #4C7ABB);");
            dialogPane.getStylesheets().add(
               getClass().getResource("dialog.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-pane");
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.get() == ButtonType.OK)
            {

                loginPane.toFront();
                paused=true;
                clearData();
                server.disconnect();
            }
            else
            {
                
            }
        }
        if(event.getSource() == menuClear) {
            System.out.println("menuClear");
            clearData();
          }
        if(event.getSource() == menuStop) {
            System.out.println("menuStop");
            server.disconnect();
            paused=true;
            indicator.setFill(javafx.scene.paint.Color.DARKGRAY);
        }
        if(event.getSource() == menuReconnect) {
            System.out.println("menuReconnect");
            server.disconnect();
            paused=false;
            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

            connect(host, port, username, password);
        }
        if(event.getSource() == menuAbout) {
            System.out.println("menuAbout");
            try {
                if(aboutPnl.isOn)
                {
                    aboutPnl.bringUp();
                }
                else
                {
                    aboutPnl = new About();
                    aboutPnl.showWindow();
                }
                
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private void connectionLost()
    {
        System.out.println("connectionLost");
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.initStyle(StageStyle.TRANSPARENT);
        //dialog.setTitle("Server Monitor Alert");
        dialog.setHeaderText("Connection lost");
        dialog.setContentText("   The connection was inturupted. Would like to you try again?");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom left, #42ABCB, #4C7ABB);");
        dialogPane.getStylesheets().add(
           getClass().getResource("dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        dialog.setDialogPane(dialogPane);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.get() == ButtonType.OK)
        {
            server.disconnect();

            try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

            connect(host, port, username, password);
        }
        else
        {

            server.disconnect();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sshHost.setText("115.23.235.135");
        sshPort.setText("443");
        sshPassword.setText("junu8035");
        sshUsername.setText("gs19007");
        labelFailed.setOpacity(0.0);
        loginPane.toFront();
        

        series_mem = new XYChart.Series<>();
        series_cpu = new XYChart.Series<>();
        
        for(int i = 0; i < 6; i++)
            series_gpumem[i] = new XYChart.Series<>();
        for(int i = 0; i < 6; i++)
            series_gpuuse[i] = new XYChart.Series<>();
        

        cpuchart.getData().add(series_cpu);
        cpuchart.setLegendVisible(false);
        ramchart.getData().add(series_mem);
        ramchart.setLegendVisible(false);
        
        for(int i = 0; i < 6; i++)
            gpumemchart.getData().add(series_gpumem[i]);

        for(int i = 0; i < 6; i++)
            gpuusechart.getData().add(series_gpuuse[i]);
        
        gpumemchart.setLegendVisible(false);
        gpuusechart.setLegendVisible(false);
        
        cpuchart.getXAxis().setTickLabelsVisible(false);
        cpuchart.getXAxis().setOpacity(0);

        ramchart.getXAxis().setTickLabelsVisible(false);
        ramchart.getXAxis().setOpacity(0);

        gpumemchart.getXAxis().setTickLabelsVisible(false);
        gpumemchart.getXAxis().setOpacity(0);
        
        gpuusechart.getXAxis().setTickLabelsVisible(false);
        gpuusechart.getXAxis().setOpacity(0);
        
        docWebEngine = docWeb.getEngine();
        docWebEngine.load("https://pdfhost.io/v/TEdtAYRLB_GSHS_Linux_Intro.pdf");

        hostWebEngine = hostView.getEngine();
        hostWebEngine.load("http://115.23.235.137:80/");
        
        viewHelp.getEngine().setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
            @Override
            public WebEngine call(PopupFeatures p) {
                Stage stage = new Stage(StageStyle.UTILITY);
                WebView wv2 = new WebView();
                stage.setScene(new Scene(wv2));
                stage.show();
                return wv2.getEngine();
            }
        });
        viewHelp.getEngine().load("http://naver.me/GnGhdXk5");
                
        filePathPC.setText("C:\\Users\\junuk\\OneDrive\\Desktop\\");
        filePathServer.setText("/home/gs19007/test/");
    }    
    
    public static void setAbout(boolean b)
    {
        aboutPnl.isOn = b;
    }
    
    private void clearData()
    {
        series_cpu.getData().clear();
        series_mem.getData().clear();
        for(int i = 0; i < 6; i++)
            series_gpumem[i].getData().clear();
        for(int i = 0; i < 6; i++)
            series_gpuuse[i].getData().clear();
    }
    
    public boolean connect(String host, int port, String username, String password) {
        System.out.println("START");
        paused=false;
        server = new SSHClient();
        
        if(server.sshAccess(host, port, username, password))
        {
            Thread resReader = new Thread(new ResReader());
            resReader.start();
            System.out.println("TH OK");
            labelFailed.setOpacity(0.0);
            boardHost.setText(host);
            boardPort.setText(""+port);
            boardUsername.setText(username);
            System.out.println("SET OK");
            if(username.substring(0, 2).equals("gs"))
            {
                String str = username.substring(2, 4);
                str = str.replaceAll("[^\\d]", "");
                boardDivision.setText("GSHS"+(Integer.parseInt(str)+18));
                str = username.substring(2, 7);
                str = str.replaceAll("[^\\d]", "");
                boardID.setText(""+Integer.parseInt(str));
            }
            else
            {
                boardDivision.setText("unknown");
                boardID.setText("unknown");
            }
            System.out.println("SET OK 2");
            now = Calendar.getInstance(Locale.KOREA);
            
            Date today = new Date();
            boardStart.setText(""+today);
            System.out.println(Calendar.HOUR_OF_DAY+":"+Calendar.MINUTE+":"+Calendar.SECOND);
            
            lastupdate = startTime = System.currentTimeMillis();
            System.out.println("Login OK");
            
            webField.setText("http://115.23.235.137:80/");

            Thread statReader = new Thread(new StatusReader());
            statReader.start();
            return true;
        }
        else
        {
            System.out.println("SSH Connection Failed");
            labelFailed.setOpacity(1.0);
            System.out.println("Label true");
            return false;
        }
    }
    
    class ResReader implements Runnable {
        @Override
        public void run() {

            try {
                while (true) {

                    
                    
                    if(ctr > 120)
                    {
                        series_cpu.getData().remove(0);
                        series_mem.getData().remove(0);
                        for(int i = 0; i < 6; i++)
                        {
                            series_gpumem[i].getData().remove(0);
                            series_gpuuse[i].getData().remove(0);
                        }
                    }
                    double[] gpu_usage = getGPUUsage();
                    
                    for(int i = 0; i < 6; i++)
                        series_gpumem[i].getData().add(new XYChart.Data<>(ctr + "", gpu_usage[i]));
                    for(int i = 6; i < 12; i++)
                        series_gpuuse[i-6].getData().add(new XYChart.Data<>(ctr + "", gpu_usage[i]));
                    series_mem.getData().add(new XYChart.Data<>(ctr + "", getMEMUsage()));
                    series_cpu.getData().add(new XYChart.Data<>(ctr + "", getCPUUsage()));
                    ctr++;
                    
                    lastupdate = System.currentTimeMillis();
                    
                    System.out.println("Step: "+ctr);
                    
                    Thread.sleep(1000);
                    
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double getMEMUsage() throws Exception
        {
            Pattern mem_total_patt = Pattern.compile("MemTotal:       ........");
            Pattern mem_avail_patt = Pattern.compile("MemAvailable:   ........");
            
            String meminfo = server.Exec("cat /proc/meminfo");
            
            InputStream is = new ByteArrayInputStream(meminfo.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            
            String line = "";
            double mem_total = 0, mem_avail = 0, mem_usage = 0;
            while ((line = reader.readLine()) != null) {
                Matcher mem_total_match = mem_total_patt.matcher(line);
                Matcher mem_avail_match = mem_avail_patt.matcher(line);
                if (mem_total_match.find()) {
                    //System.out.println("mem_match_total Found = " + mem_total_match.group().substring(1));
                    mem_total = (double)Integer.parseInt(mem_total_match.group().substring(16));
                }
                if (mem_avail_match.find()) {
                    //System.out.println("mem_match_avail Found = " + mem_avail_match.group().substring(1));
                    mem_avail = (double)Integer.parseInt(mem_avail_match.group().substring(16));
                }
                if(mem_total > 0 && mem_avail > 0)
                {
                    mem_usage = (1.00 - mem_avail / mem_total) * 100.0;
                    break;
                    
                }
            }
            return mem_usage;
        }
        double getCPUUsage() throws Exception
        {
            String cpuinfo = server.Exec("cat /proc/stat|grep \"^cpu* \"");
            cpuinfo = cpuinfo.substring(5);
            InputStream is = new ByteArrayInputStream(cpuinfo.getBytes());
            //System.out.println(cpuinfo);
            Scanner cpuscan = new Scanner(is);
            double[] arr = new double[10];
            double total = 0;
            for (int i = 0; i < 10; i++) {
                //System.out.println(i);
                arr[i] = (double)cpuscan.nextLong();
                total += (double)arr[i];
            }
            double cpu_usage = (double)(1.00 - arr[3]/total)*100.0;
            //System.out.println("cpu_usage: " + cpu_usage);
            //cpu_t[ctr % 120] = cpu_usage;
            cpuscan.close();
            return cpu_usage;
        }
        double[] getGPUUsage() throws Exception
        {
            double[] gpu_usage = new double[15];
            Pattern gpu_patt_mb = Pattern.compile(".....MiB / .....MiB");
            Pattern gpu_use_patt = Pattern.compile("..%      Default");
            

            String gpuinfo = server.Exec("nvidia-smi");
            //System.out.println(meminfo);
            int gpunum = 0;
            InputStream is = new ByteArrayInputStream(gpuinfo.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            
            String line = "";
            double gpu_total = 0, gpu_use = 0;
            while ((line = reader.readLine()) != null) {
                Matcher gpu_match = gpu_patt_mb.matcher(line);
                if (gpu_match.find()) {
                    gpu_use = (double)Integer.parseInt(gpu_match.group().substring(0, 5).replace(" ", "0"));
                    gpu_total = (double)Integer.parseInt(gpu_match.group().substring(11, 16).replace(" ", "0"));
                    gpu_usage[gpunum++] = gpu_use / gpu_total * 100.0;
                }
                if(gpunum == 6)
                    break;
            }
            

            is = new ByteArrayInputStream(gpuinfo.getBytes());
            reader = new BufferedReader(new InputStreamReader(is));
            gpunum=6;
            line = "";
            int gpu_percent = 0;
            while ((line = reader.readLine()) != null) {
                Matcher gpu_match = gpu_use_patt.matcher(line);
                if (gpu_match.find()) {
                    gpu_percent = Integer.parseInt(gpu_match.group().substring(0, 2).replace(" ", "0"));
                    gpu_usage[gpunum] = gpu_percent;
                    gpunum++;
                    //System.out.println("mem_usage: " + gpu_percent);
                }
                if(gpunum == 12)
                    break;
            }
            
            
            return gpu_usage;
        }
    }
    
    class StatusReader implements Runnable {

        @Override
        public void run() {

            try{
                while (true) {
                	if(paused)
                	{
                        indicator.setFill(javafx.scene.paint.Color.DARKGREY);
                		continue;
                	}
                    //updateTime();
                    System.out.println("Check");
                    Thread.sleep(500);
                    if((System.currentTimeMillis() - lastupdate) > 10000)
                    {
                        indicator.setFill(javafx.scene.paint.Color.RED);
                    }
                    else if((System.currentTimeMillis() - lastupdate) > 5000)
                    {
                        indicator.setFill(javafx.scene.paint.Color.YELLOW);
                    }
                    else if((System.currentTimeMillis() - lastupdate) < 5000)
                    {
                        indicator.setFill(javafx.scene.paint.Color.GREEN);
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
