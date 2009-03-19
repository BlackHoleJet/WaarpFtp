/**
 * Frederic Bregier LGPL 2 f�vr. 09 FtpInternalLoggerFactory.java
 * goldengate.ftp.core.logging GoldenGateFtp frederic
 */
package goldengate.ftp.core.logging;

/**
 * Based on the Netty InternalLoggerFactory Based on The Netty Project
 * (netty-dev@lists.jboss.org)
 * 
 * @author Trustin Lee (tlee@redhat.com)
 * @author frederic goldengate.ftp.core.logging FtpInternalLoggerFactory
 * 
 */
public abstract class FtpInternalLoggerFactory extends
        org.jboss.netty.logging.InternalLoggerFactory {
    /**
     * 
     * @param clazz
     * @return the FtpInternalLogger
     */
    public static FtpInternalLogger getLogger(Class<?> clazz) {
        return (FtpInternalLogger) getDefaultFactory().newInstance(
                clazz.getName());
    }

}
