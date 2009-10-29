/**
 * Copyright 2009, Frederic Bregier, and individual contributors by the @author
 * tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3.0 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package goldengate.ftp.simpleimpl.control;

import java.io.File;

import goldengate.common.command.exception.CommandAbstractException;
import goldengate.common.command.exception.Reply502Exception;
import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import goldengate.ftp.core.command.AbstractCommand;
import goldengate.ftp.core.command.FtpCommandCode;
import goldengate.ftp.core.command.service.MKD;
import goldengate.ftp.core.control.BusinessHandler;
import goldengate.ftp.core.data.FtpTransfer;
import goldengate.ftp.core.file.FtpFile;
import goldengate.ftp.core.session.FtpSession;
import goldengate.ftp.filesystembased.FilesystemBasedFtpAuth;
import goldengate.ftp.filesystembased.FilesystemBasedFtpRestart;
import goldengate.ftp.simpleimpl.file.FileBasedAuth;
import goldengate.ftp.simpleimpl.file.FileBasedDir;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ExceptionEvent;

/**
 * BusinessHandler implementation that allows pre and post actions on any
 * operations and specifically on transfer operations
 *
 * @author Frederic Bregier
 *
 */
public class SimpleBusinessHandler extends BusinessHandler {
    /**
     * Internal Logger
     */
    private static final GgInternalLogger logger = GgInternalLoggerFactory
            .getLogger(SimpleBusinessHandler.class);

    @Override
    public void afterRunCommandKo(CommandAbstractException e) {
        // TODO Auto-generated method stub
        if (getFtpSession().getCurrentCommand() instanceof MKD) {
            // do nothing
        } else {
            logger.warn("GBBH: AFTKO: {} {}", getFtpSession(), e.getMessage());
        }
    }

    @Override
    public void afterRunCommandOk() throws CommandAbstractException {
        // TODO Auto-generated method stub
        // logger.info("GBBH: AFTOK: {}", getFtpSession());
        if (getFtpSession().getCurrentCommand().getCode() == FtpCommandCode.STOR) {
            String dir = ((FilesystemBasedFtpAuth) getFtpSession().getAuth()).getBaseDirectory();
            String filename = getFtpSession().getCurrentCommand().getArg();
            FtpFile file = getFtpSession().getDir().setFile(filename, false);
            String path = file.getFile();
            logger.warn("File is not readable: "+dir+" "+path+" "+
                    ((new File(dir+path)).canRead()));
        }
    }

    @Override
    public void beforeRunCommand() throws CommandAbstractException {
        // TODO Auto-generated method stub
        // logger.info("GBBH: BEFCD: {}", getFtpSession());
    }

    @Override
    protected void cleanSession() {
        // TODO Auto-generated method stub
        // logger.info("GBBH: CLNSE: {}", getFtpSession());
    }

    @Override
    public void exceptionLocalCaught(ExceptionEvent e) {
        // TODO Auto-generated method stub
        logger.warn("GBBH: EXCEP: {} {}", getFtpSession(), e.getCause()
                .getMessage());
    }

    @Override
    public void executeChannelClosed() {
        // TODO Auto-generated method stub
        logger.warn("GBBH: CLOSED: for user {} with session {} ",
                getFtpSession().getAuth().getUser(), getFtpSession());
    }

    @Override
    public void executeChannelConnected(Channel channel) {
        // TODO Auto-generated method stub
        // logger.info("GBBH: CONNEC: {}", getFtpSession());
    }

    @Override
    public FileBasedAuth getBusinessNewAuth() {
        return new FileBasedAuth(getFtpSession());
    }

    @Override
    public FileBasedDir getBusinessNewDir() {
        return new FileBasedDir(getFtpSession());
    }

    @Override
    public FilesystemBasedFtpRestart getBusinessNewRestart() {
        return new FilesystemBasedFtpRestart(getFtpSession());
    }

    @Override
    public void afterTransferDone(FtpTransfer transfer) {
        if (transfer.getCommand() == FtpCommandCode.APPE) {
            logger.info("GBBH: Transfer: {} " + transfer.getStatus() + " {}",
                    transfer.getCommand(), transfer.getPath());
        } else if (transfer.getCommand() == FtpCommandCode.RETR) {
            logger.info("GBBH: Transfer: {} " + transfer.getStatus() + " {}",
                    transfer.getCommand(), transfer.getPath());
        } else if (transfer.getCommand() == FtpCommandCode.STOR) {
            logger.info("GBBH: Transfer: {} " + transfer.getStatus() + " {}",
                    transfer.getCommand(), transfer.getPath());
        } else if (transfer.getCommand() == FtpCommandCode.STOU) {
            logger.info("GBBH: Transfer: {} " + transfer.getStatus() + " {}",
                    transfer.getCommand(), transfer.getPath());
        } else {
            logger.warn("GBBH: Transfer unknown: {} " + transfer.getStatus() +
                    " {}", transfer.getCommand(), transfer.getPath());
            // Nothing to do
        }
    }

    @Override
    public String getHelpMessage(String arg) {
        return "This FTP server is only intend as a Gateway.\n"
                + "This FTP server refers to RFC 959, 775, 2389, 2428, 3659 and supports XCRC, XMD5 and XSHA1 commands.\n"
                + "XCRC, XMD5 and XSHA1 take a simple filename as argument and return \"250 digest-value is the digest of filename\".";
    }

    @Override
    public String getFeatMessage() {
        StringBuilder builder = new StringBuilder("Extensions supported:");
        builder.append('\n');
        builder.append(getDefaultFeatMessage());
        builder.append("\nEnd");
        return builder.toString();
    }

    @Override
    public String getOptsMessage(String[] args) throws CommandAbstractException {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(FtpCommandCode.MLST.name()) ||
                    args[0].equalsIgnoreCase(FtpCommandCode.MLSD.name())) {
                return getMLSxOptsMessage(args);
            }
            throw new Reply502Exception("OPTS not implemented for " + args[0]);
        }
        throw new Reply502Exception("OPTS not implemented");
    }

    /* (non-Javadoc)
     * @see goldengate.ftp.core.control.BusinessHandler#getSpecializedSiteCommand(goldengate.ftp.core.session.FtpSession, java.lang.String)
     */
    @Override
    public AbstractCommand getSpecializedSiteCommand(FtpSession session,
            String line) {
        return null;
    }
}
