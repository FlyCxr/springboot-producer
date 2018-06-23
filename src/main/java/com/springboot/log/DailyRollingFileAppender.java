package com.springboot.log;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.QuietWriter;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;

/**
 * 真正的日志处理器
 * 继承自实现的PuffLoggerFileAppender。异步实现日志操作
 * @author dongyangyang
 */
@SuppressWarnings("all")
public class DailyRollingFileAppender extends PuffLoggerFileAppender {
	
    public DailyRollingFileAppender() {
        super();
        Runtime.getRuntime().addShutdownHook(new Log4jHookThread());
    }

    public DailyRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
    	super(layout, filename, datePattern);
        Runtime.getRuntime().addShutdownHook(new Log4jHookThread());
    }

    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
            throws IOException {
        File logfile = new File(fileName);

        logfile.getParentFile().mkdirs();

        super.setFile(fileName, append, bufferedIO, bufferSize);
    }

    public QuietWriter getQw() {
        return super.qw;
    }

    private class Log4jHookThread extends Thread {
        @Override
        public void run() {
            QuietWriter qw = DailyRollingFileAppender.this.getQw();
            if (qw != null) {
                qw.flush();
            }
        }
    }
}

