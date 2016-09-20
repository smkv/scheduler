import ee.smkv.scheduler.Application;
import ee.smkv.scheduler.Config;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Launcher {

    private static final Logger LOG = Logger.getLogger(Launcher.class);
    private static final String MACHINE_NAME = Config.getProperty(Application.MACHINE_NAME_CONFIG_PARAMETER);

    public static void main(String[] args) throws Exception {
        NDC.push("MAIN");
        LOG.info(String.format("Staring scheduler for machine '%s'", MACHINE_NAME));
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        new AnnotationConfigApplicationContext(Application.class);
    }

    private static class ShutdownHook extends Thread {

        @Override
        public void run() {
            LOG.info(String.format("Stopping scheduler for machine '%s'", MACHINE_NAME));
        }

    }
}
