import ee.smkv.scheduler.Application;
import ee.smkv.scheduler.Config;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Launcher {

    public static void main(String[] args) throws Exception {
        NDC.push("MAIN");
        Logger logger = Logger.getLogger(Launcher.class);
        String machineName = Config.getProperty(Application.MACHINE_NAME_CONFIG_PARAMETER);
        logger.info(String.format("Staring scheduler for machine '%s'", machineName));

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                logger.info(String.format("Stopping scheduler for machine '%s'", machineName));
            }
        });

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(Application.class);
        applicationContext.refresh();
    }
}
