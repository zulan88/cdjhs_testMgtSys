package cn.net.wanji.testmgtsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/**
 * 启动程序!
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class WanjiDataApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(WanjiDataApplication.class, args);
    }
}
