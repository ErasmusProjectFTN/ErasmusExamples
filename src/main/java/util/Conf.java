package util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.jena.base.Sys;

/**
 * This will be bean or something like that
 */
public class Conf {
    private static Conf instance = null;
    private Config conf;
    private String pdfRoot;
    private String storageRoot;


    private Conf(){
        initialize();
    }

    private void initialize(){
        this.conf = ConfigFactory.load("application.conf");
        this.storageRoot = conf.getConfig("storage").getString("store_root");
        this.pdfRoot = storageRoot +  conf.getConfig("storage").getConfig("pdf").getString("root");
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

    public String getStorageRoot(){
        return this.storageRoot;
    }

    public static void main(String[] args){
        Conf c = Conf.getInstance();

        System.out.println(c.getPdfRoot());
        System.out.println(c.getStorageRoot());
    }
}
