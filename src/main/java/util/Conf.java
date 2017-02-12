package util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * This will be bean or something like that
 */
public class Conf {
    private static Conf instance = null;
    private Config conf;
    private String pdfRoot;


    private Conf(){
        initialize();
    }

    private void initialize(){
        this.conf = ConfigFactory.load("application.conf");
        this.pdfRoot = conf.getString("pdf_root");
    }

    public static Conf getInstance(){
        if(instance == null){
            instance = new Conf();
        }
        return instance;
    }

    public String getPdfRoot(){
        return this.pdfRoot;
    }

    public static void main(String[] args){
        Conf c = Conf.getInstance();

        System.out.println(c.getPdfRoot());
    }
}
