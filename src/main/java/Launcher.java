import ee.smkv.scheduler.Application;
import ee.smkv.scheduler.Config;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Launcher {

    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(Launcher.class);
        NDC.push("MAIN");
        logger.info("Staring scheduler for machine " + Config.getProperty(Application.MACHINE_NAME_CONFIG_PARAMETER));
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(Application.class);
        applicationContext.refresh();
    }
}
