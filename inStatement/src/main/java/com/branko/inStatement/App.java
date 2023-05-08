package com.branko.inStatement;

import java.awt.Toolkit;	
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Locale;	
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application
{
	
	
    public static void main( String[] args )
    {
       launch();
    }
    
   
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		TextArea taOrginal = new TextArea() {
            @Override
            public void paste() {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                if (clipboard.hasString()) {
                    replaceSelection(getStringFromClipboard());
                }
            }
        };
        
		TextArea taIN = new TextArea();
		taOrginal.setPrefSize(245, 500);
		taIN.setPrefSize(245, 500);
		taOrginal.setLayoutX(5);
		taOrginal.setLayoutY(0);
		taIN.setLayoutX(260);
		taIN.setLayoutY(0);
		
		AnchorPane pane = new AnchorPane();
	    pane.getChildren().addAll(taOrginal,taIN);
	      
		taOrginal.textProperty().addListener(new ChangeListener<String>() 
		{
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) 
		    {
		    	String[] newValue2 = newValue.split("\n");
		    	StringBuilder sb=new StringBuilder(); 
		    	
		    	for(String x : newValue2) 
		    	{
		    				sb.append("'"+x+"',\n");		
		    	}
		    	
		    	taIN.setText("IN ( "+sb.toString().substring(0,sb.toString().length()-2)+" )");
		    }
		});
		 
		primaryStage.setScene(new Scene(pane,500,500));
		primaryStage.setTitle("IN Statement by Branko");
		primaryStage.getIcons().add(new Image("gift-box.png"));
		primaryStage.setResizable(false);
		primaryStage.show();
		
		taIN.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
	        @Override
	        public void handle(MouseEvent e) {
	        	
	        	final Clipboard clipboard = Clipboard.getSystemClipboard();
	            final ClipboardContent content = new ClipboardContent();
	            content.putString(taIN.getText());
	            clipboard.setContent(content);
	            
	            showPopupMessage("Copied to clipboard !",primaryStage);
	            
	        }
		});
		
	}
	
	public static Popup createPopup(final String message) 
	{
	    final Popup popup = new Popup();
	    popup.setAutoFix(true);
	    popup.setAutoHide(true);
	    popup.setHideOnEscape(true);
	    Label label = new Label(message);
	    label.setOnMouseReleased(new EventHandler<MouseEvent>() 
	    {
	        @Override
	        public void handle(MouseEvent e) 
	        {
	            popup.hide();
	        }
	    });
	    label.getStylesheets().add("styles.css");
	    label.getStyleClass().add("popup");
	    popup.getContent().add(label);
	    return popup;
	}

	public static void showPopupMessage(final String message, final Stage stage) 
	{
	    final Popup popup = createPopup(message);
	    popup.setOnShown(new EventHandler<WindowEvent>() 
	    {
	        @Override
	        public void handle(WindowEvent e) 
	        {
	            popup.setX(stage.getX() + stage.getWidth()/2 - popup.getWidth()/2);
	            popup.setY(stage.getY() + stage.getHeight()/2 - popup.getHeight()/2);
	        }
	    });        
	    popup.show(stage);
	}
	
	static 
	{
	    Locale.setDefault(new Locale("US"));
	}
	
	public static String getStringFromClipboard() 
	{
		  Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		 
		  try 
		  {
			  if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) 
			  {
				  String text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
				  return text;
			  }
		  
		  }
		  catch (UnsupportedFlavorException e) 
		  {
		 
		    	System.out.println("Clipboard content flavor is not supported " + e.getMessage());
		  
		  }
		  catch (IOException e) 
		  {
			  System.out.println("Clipboard content could not be retrieved " + e.getMessage());
		  }
		     return null;
		  }
}
