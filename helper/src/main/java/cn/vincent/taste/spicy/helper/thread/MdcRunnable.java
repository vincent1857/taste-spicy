package cn.vincent.taste.spicy.helper.thread;

import org.slf4j.MDC;

import java.util.Map;

/**
 * @author vincent
 * @version 1.0 2020/3/15 19:30
 */
public class MdcRunnable implements Runnable {

    private final Runnable runnable;

    private transient final Map<String, String> cm = MDC.getCopyOfContextMap();

    public MdcRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if (cm != null) {
            MDC.setContextMap(cm);
        }
        try {
            runnable.run();
        } finally {
            MDC.clear();
        }
    }
}
